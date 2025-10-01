package com.android.technicaltest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.technicaltest.domain.model.ConnectionStatus
import com.android.technicaltest.domain.model.SyncState
import com.android.technicaltest.domain.model.SyncStatus
import com.android.technicaltest.domain.usecase.SyncUseCase
import com.android.technicaltest.util.NetworkMonitor
import com.android.technicaltest.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncViewModel(
    private val syncUseCase: SyncUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _syncStatus = MutableStateFlow(
        SyncStatus(
            connectionStatus = ConnectionStatus.Offline,
            syncState = SyncState.Idle,
            pendingOperationsCount = 0
        )
    )
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    init {
        observeNetworkStatus()
        observePendingOperations()
        startAutoSync()
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.observeConnectivity().collect { isConnected ->
                val connectionStatus = if (isConnected) {
                    ConnectionStatus.Online
                } else {
                    ConnectionStatus.Offline
                }

                _syncStatus.update { it.copy(connectionStatus = connectionStatus) }

                // Auto-sync when coming online
                if (isConnected && _syncStatus.value.pendingOperationsCount > 0) {
                    sync()
                }
            }
        }
    }

    private fun observePendingOperations() {
        viewModelScope.launch {
            syncUseCase.getPendingOperationsCount().collect { count ->
                _syncStatus.update { it.copy(pendingOperationsCount = count.toInt()) }
            }
        }
    }

    private fun startAutoSync() {
        viewModelScope.launch {
            while (true) {
                delay(30000) // Every 30 seconds

                if (_syncStatus.value.connectionStatus is ConnectionStatus.Online &&
                    _syncStatus.value.pendingOperationsCount > 0) {
                    sync()
                }
            }
        }
    }

    fun sync() {
        viewModelScope.launch {
            if (_syncStatus.value.connectionStatus is ConnectionStatus.Offline) {
                return@launch
            }

            _syncStatus.update { it.copy(syncState = SyncState.Syncing) }

            when (val result = syncUseCase()) {
                is Result.Success -> {
                    val now = kotlin.time.Clock.System.now()
                    _syncStatus.update {
                        it.copy(
                            syncState = SyncState.Success(now),
                            lastSyncTime = now
                        )
                    }

                    // Reset to idle after 3 seconds
                    delay(3000)
                    _syncStatus.update { it.copy(syncState = SyncState.Idle) }
                }
                is Result.Error -> {
                    _syncStatus.update {
                        it.copy(syncState = SyncState.Error(result.message))
                    }

                    // Reset to idle after 5 seconds
                    delay(5000)
                    _syncStatus.update { it.copy(syncState = SyncState.Idle) }
                }
                else -> {}
            }
        }
    }
}
