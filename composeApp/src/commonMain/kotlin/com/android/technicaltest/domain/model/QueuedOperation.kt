package com.android.technicaltest.domain.model

import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

enum class OperationType {
    CREATE, UPDATE, DELETE
}

enum class OperationStatus {
    PENDING, PROCESSING, FAILED
}

@OptIn(ExperimentalTime::class)
data class QueuedOperation(
    val id: Long?,
    val operationType: OperationType,
    val productId: Long?,
    val title: String?,
    val description: String?,
    val userId: String,
    val createdAt: Instant,
    val retryCount: Int = 0,
    val lastError: String? = null,
    val status: OperationStatus = OperationStatus.PENDING
)
