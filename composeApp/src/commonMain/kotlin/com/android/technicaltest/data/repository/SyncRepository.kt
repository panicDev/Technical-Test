package com.android.technicaltest.data.repository

import com.android.technicaltest.data.local.ProductDatabase
import com.android.technicaltest.data.local.QueueDatabase
import com.android.technicaltest.data.remote.ProductApi
import com.android.technicaltest.data.remote.dto.CreateProductRequest
import com.android.technicaltest.data.remote.dto.UpdateProductRequest
import com.android.technicaltest.domain.model.OperationStatus
import com.android.technicaltest.domain.model.OperationType
import com.android.technicaltest.domain.model.Product
import com.android.technicaltest.domain.model.QueuedOperation
import com.android.technicaltest.util.NetworkMonitor
import com.android.technicaltest.util.Result
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncRepository(
    private val queueDatabase: QueueDatabase,
    private val productDatabase: ProductDatabase,
    private val productApi: ProductApi,
    private val networkMonitor: NetworkMonitor
) {

    suspend fun syncPendingOperations(): Result<Unit> {
        if (!networkMonitor.checkConnection()) {
            return Result.Error("No internet connection")
        }

        val pendingOps = queueDatabase.getAllPendingOperations().first()

        for (operation in pendingOps) {
            val result = processOperation(operation)

            when (result) {
                is Result.Success -> {
                    // Remove from queue
                    queueDatabase.deleteOperation(operation.id!!)

                    // Mark product as synced if applicable
                    operation.productId?.let { productId ->
                        if (operation.operationType != OperationType.DELETE) {
                            productDatabase.markAsSynced(productId)
                        } else {
                            // Actually delete from local DB now
                            productDatabase.deleteProduct(productId)
                        }
                    }
                }
                is Result.Error -> {
                    // Update retry count
                    queueDatabase.updateOperationStatus(
                        id = operation.id!!,
                        status = OperationStatus.FAILED,
                        error = result.message,
                        retryCount = operation.retryCount + 1
                    )
                }
                else -> {}
            }
        }

        return Result.Success(Unit)
    }

    private suspend fun processOperation(operation: QueuedOperation): Result<Unit> {
        return when (operation.operationType) {
            OperationType.CREATE -> {
                val result = productApi.createProduct(
                    operation.userId,
                    CreateProductRequest(operation.title!!, operation.description)
                )

                when (result) {
                    is Result.Success -> {
                        // Update local product with server ID
                        operation.productId?.let { tempId ->
                            if (tempId < 0) {
                                productDatabase.deleteProduct(tempId)
                                val product = result.data
                                val localProduct = Product(
                                    id = product.id,
                                    title = product.title,
                                    description = product.description,
                                    userId = product.userId,
                                    createdAt = product.createdAt?.let { Instant.parse(it) }
                                        ?: Clock.System.now(),
                                    updatedAt = product.updatedAt?.let { Instant.parse(it) }
                                        ?: Clock.System.now(),
                                    isSynced = true,
                                    lastSyncedAt = Clock.System.now()
                                )
                                productDatabase.insertProduct(localProduct)
                            }
                        }
                        Result.Success(Unit)
                    }
                    is Result.Error -> result.copy(message = result.message)
                    else -> Result.Error("Unexpected result")
                }
            }

            OperationType.UPDATE -> {
                val result = productApi.updateProduct(
                    operation.userId,
                    operation.productId!!,
                    UpdateProductRequest(operation.title!!, operation.description)
                )

                when (result) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Error -> result.copy(message = result.message)
                    else -> Result.Error("Unexpected result")
                }
            }

            OperationType.DELETE -> {
                val result = productApi.deleteProduct(operation.userId, operation.productId!!)

                when (result) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Error -> result
                    else -> Result.Error("Unexpected result")
                }
            }
        }
    }

    fun getPendingOperationsCount() = queueDatabase.getPendingOperationsCount()
}