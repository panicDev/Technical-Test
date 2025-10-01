package com.android.technicaltest.domain.model

import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
data class Product(
    val id: Long?,
    val title: String,
    val description: String?,
    val userId: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isSynced: Boolean = true,
    val lastSyncedAt: Instant? = null
)