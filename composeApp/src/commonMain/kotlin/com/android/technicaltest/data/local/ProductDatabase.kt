package com.android.technicaltest.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.SqlDriver
import com.android.technicaltest.database.AppDatabase
import com.android.technicaltest.database.ProductEntity
import com.android.technicaltest.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class ProductDatabase(
    sqlDriver: SqlDriver
) {
    private val database = AppDatabase(sqlDriver)
    val queries = database.productQueries

    fun getAllProducts(): Flow<List<Product>> {
        return queries.getAllProducts()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }
    }

    fun getProductById(id: Long): Flow<Product?> {
        return queries.getProductById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return queries.searchProducts(query, query)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }
    }

    fun getUnsyncedProducts(): Flow<List<Product>> {
        return queries.getUnsyncedProducts()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }
    }

    suspend fun insertProduct(product: Product) {
        queries.insertProduct(
            id = product.id,
            title = product.title,
            description = product.description,
            userId = product.userId,
            createdAt = product.createdAt.toString(),
            updatedAt = product.updatedAt.toString(),
            isSynced = product.isSynced,
            lastSyncedAt = product.lastSyncedAt?.toString()
        )
    }

    suspend fun updateProduct(id: Long, title: String, description: String?, isSynced: Boolean) {
        queries.updateProduct(
            title = title,
            description = description,
            updatedAt = Instant.fromEpochMilliseconds(System.currentTimeMillis()).toString(),
            isSynced = isSynced,
            id = id
        )
    }

    suspend fun deleteProduct(id: Long) {
        queries.deleteProduct(id)
    }

    suspend fun markAsSynced(id: Long) {
        queries.markAsSynced(
            lastSyncedAt = Instant.fromEpochMilliseconds(System.currentTimeMillis()).toString(),
            id = id
        )
    }

    suspend fun clearAll() {
        queries.clearAll()
    }

    private fun ProductEntity.toDomain(): Product {
        return Product(
            id = id,
            title = title,
            description = description,
            userId = userId,
            createdAt = Instant.parse(createdAt),
            updatedAt = Instant.parse(updatedAt),
            isSynced = isSynced,
            lastSyncedAt = lastSyncedAt?.let { Instant.parse(it) }
        )
    }
}