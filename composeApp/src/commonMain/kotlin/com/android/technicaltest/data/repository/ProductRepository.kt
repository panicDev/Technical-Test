package com.android.technicaltest.data.repository

import com.android.technicaltest.data.local.ProductDatabase
import com.android.technicaltest.data.local.QueueDatabase
import com.android.technicaltest.data.remote.ProductApi
import com.android.technicaltest.data.remote.dto.CreateProductRequest
import com.android.technicaltest.data.remote.dto.ProductDto
import com.android.technicaltest.data.remote.dto.UpdateProductRequest
import com.android.technicaltest.domain.model.OperationType
import com.android.technicaltest.domain.model.Product
import com.android.technicaltest.domain.model.QueuedOperation
import com.android.technicaltest.util.NetworkMonitor
import com.android.technicaltest.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ProductRepository(
    private val productDatabase: ProductDatabase,
    private val queueDatabase: QueueDatabase,
    private val productApi: ProductApi,
    private val networkMonitor: NetworkMonitor,
) {

    private val userId = "YOUR_USER_ID"

    fun getAllProducts(): Flow<List<Product>> {
        return productDatabase.getAllProducts()
    }

    fun getProductById(id: Long): Flow<Product?> {
        return productDatabase.getProductById(id)
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDatabase.searchProducts(query)
    }

    suspend fun createProduct(title: String, description: String?): Result<Product> {
        val now = kotlin.time.Clock.System.now()
        val isOnline = networkMonitor.checkConnection()

        return if (isOnline) {
            // Try to create online
            when (val result = productApi.createProduct(userId,
                CreateProductRequest(title, description)
            )) {
                is Result.Success -> {
                    val product = result.data.toDomain(userId)
                    productDatabase.insertProduct(product)
                    Result.Success(product)
                }
                is Result.Error -> {
                    // If failed, queue it
                    createOfflineProduct(title, description, now)
                }
                else -> Result.Error("Unexpected result")
            }
        } else {
            // Queue for later
            createOfflineProduct(title, description, now)
        }
    }

    private suspend fun createOfflineProduct(title: String, description: String?, now: Instant): Result<Product> {
        // Create temporary local product
        val tempId = -(System.currentTimeMillis()) // Negative ID for local-only products
        val product = Product(
            id = tempId,
            title = title,
            description = description,
            userId = userId,
            createdAt = now,
            updatedAt = now,
            isSynced = false
        )

        productDatabase.insertProduct(product)

        // Queue the operation
        val operation = QueuedOperation(
            id = null,
            operationType = OperationType.CREATE,
            productId = tempId,
            title = title,
            description = description,
            userId = userId,
            createdAt = now
        )
        queueDatabase.insertOperation(operation)

        return Result.Success(product)
    }

    suspend fun updateProduct(id: Long, title: String, description: String?): Result<Product> {
        val isOnline = networkMonitor.checkConnection()

        return if (isOnline && id > 0) {
            // Try to update online (only if it's a real server ID)
            when (val result = productApi.updateProduct(userId, id,
                UpdateProductRequest(title, description)
            )) {
                is Result.Success -> {
                    val product = result.data.toDomain(userId)
                    productDatabase.updateProduct(id, title, description, true)
                    Result.Success(product)
                }
                is Result.Error -> {
                    // If failed, queue it
                    updateOfflineProduct(id, title, description)
                }
                else -> Result.Error("Unexpected result")
            }
        } else {
            // Queue for later
            updateOfflineProduct(id, title, description)
        }
    }

    private suspend fun updateOfflineProduct(id: Long, title: String, description: String?): Result<Product> {
        productDatabase.updateProduct(id, title, description, false)

        val operation = QueuedOperation(
            id = null,
            operationType = OperationType.UPDATE,
            productId = id,
            title = title,
            description = description,
            userId = userId,
            createdAt = kotlin.time.Clock.System.now()
        )
        queueDatabase.insertOperation(operation)

        val product = productDatabase.getProductById(id).first()
        return if (product != null) {
            Result.Success(product)
        } else {
            Result.Error("Product not found")
        }
    }

    suspend fun deleteProduct(id: Long): Result<Unit> {
        val isOnline = networkMonitor.checkConnection()

        return if (isOnline && id > 0) {
            // Try to delete online
            when (val result = productApi.deleteProduct(userId, id)) {
                is Result.Success -> {
                    productDatabase.deleteProduct(id)
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    // If failed, queue it
                    deleteOfflineProduct(id)
                }
                else -> Result.Error("Unexpected result")
            }
        } else {
            // Queue for later
            deleteOfflineProduct(id)
        }
    }

    private suspend fun deleteOfflineProduct(id: Long): Result<Unit> {
        // Don't delete from local DB yet, just queue
        val operation = QueuedOperation(
            id = null,
            operationType = OperationType.DELETE,
            productId = id,
            title = null,
            description = null,
            userId = userId,
            createdAt = kotlin.time.Clock.System.now()
        )
        queueDatabase.insertOperation(operation)

        // Mark as unsynced
        val product = productDatabase.getProductById(id).first()
        if (product != null) {
            productDatabase.updateProduct(id, product.title, product.description, false)
        }

        return Result.Success(Unit)
    }

    suspend fun fetchFromRemote(): Result<List<Product>> {
        return when (val result = productApi.getAllProducts(userId)) {
            is Result.Success -> {
                val products = result.data.map { it.toDomain(userId) }
                // Update local database
                products.forEach { productDatabase.insertProduct(it) }
                Result.Success(products)
            }
            is Result.Error -> result
            else -> Result.Error("Unexpected result")
        }
    }

    private fun ProductDto.toDomain(userId: String): Product {
        return Product(
            id = id,
            title = title,
            description = description,
            userId = userId,
            createdAt = createdAt?.let { Instant.parse(it) } ?: kotlin.time.Clock.System.now(),
            updatedAt = updatedAt?.let { Instant.parse(it) } ?: kotlin.time.Clock.System.now(),
            isSynced = true,
            lastSyncedAt = kotlin.time.Clock.System.now()
        )
    }
}