package com.android.technicaltest.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.db.SqlDriver
import com.android.technicaltest.database.AppDatabase
import com.android.technicaltest.database.QueuedOperationEntity
import com.android.technicaltest.domain.model.OperationStatus
import com.android.technicaltest.domain.model.OperationType
import com.android.technicaltest.domain.model.QueuedOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class QueueDatabase(sqlDriver: SqlDriver) {
    private val database = AppDatabase(sqlDriver)
    private val queries = database.queuedOperationQueries

    fun getAllPendingOperations(): Flow<List<QueuedOperation>> {
        return queries.getAllPendingOperations()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }
    }

    fun getPendingOperationsCount(): Flow<Long> {
        return queries.getPendingOperationsCount()
            .asFlow()
            .mapToOne(Dispatchers.Default)
    }

    suspend fun insertOperation(operation: QueuedOperation): Long {
        queries.insertOperation(
            operationType = operation.operationType.name,
            productId = operation.productId,
            title = operation.title,
            description = operation.description,
            userId = operation.userId,
            createdAt = operation.createdAt.toString(),
            retryCount = operation.retryCount.toLong(),
            lastError = operation.lastError,
            status = operation.status.name
        )
        return queries.getOperationById(queries.getAllPendingOperations().executeAsList().size.toLong())
            .executeAsOne().id ?: 0L
    }

    suspend fun updateOperationStatus(id: Long, status: OperationStatus, error: String?, retryCount: Int) {
        queries.updateOperationStatus(
            status = status.name,
            lastError = error,
            retryCount = retryCount.toLong(),
            id = id
        )
    }

    suspend fun deleteOperation(id: Long) {
        queries.deleteOperation(id)
    }

    suspend fun deleteOperationsByProductId(productId: Long) {
        queries.deleteOperationsByProductId(productId)
    }

    suspend fun clearAllOperations() {
        queries.clearAllOperations()
    }

    private fun QueuedOperationEntity.toDomain(): QueuedOperation {
        return QueuedOperation(
            id = id,
            operationType = OperationType.valueOf(operationType),
            productId = productId,
            title = title,
            description = description,
            userId = userId,
            createdAt = Instant.parse(createdAt),
            retryCount = retryCount.toInt(),
            lastError = lastError,
            status = OperationStatus.valueOf(status)
        )
    }
}