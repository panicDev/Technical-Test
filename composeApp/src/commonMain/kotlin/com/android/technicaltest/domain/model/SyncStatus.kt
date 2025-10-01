@file:OptIn(ExperimentalTime::class)

package com.android.technicaltest.domain.model

import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

sealed class ConnectionStatus {
    data object Online : ConnectionStatus()
    data object Offline : ConnectionStatus()
}

sealed class SyncState {
    data object Idle : SyncState()
    data object Syncing : SyncState()
    data class Error(val message: String) : SyncState()
    data class Success(val syncedAt: Instant) : SyncState()
}

data class SyncStatus(
    val connectionStatus: ConnectionStatus,
    val syncState: SyncState,
    val pendingOperationsCount: Int = 0,
    val lastSyncTime: Instant? = null
)
