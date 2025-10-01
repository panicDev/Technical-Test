package com.android.technicaltest.domain.usecase

import com.android.technicaltest.data.repository.ProductRepository
import com.android.technicaltest.data.repository.SyncRepository
import com.android.technicaltest.util.NetworkMonitor
import com.android.technicaltest.util.Result

class SyncUseCase(
    private val syncRepository: SyncRepository,
    private val productRepository: ProductRepository,
    private val networkMonitor: NetworkMonitor
) {
    suspend operator fun invoke(): Result<Unit> {
        if (!networkMonitor.checkConnection()) {
            return Result.Error("No internet connection")
        }

        // First sync pending operations
        val syncResult = syncRepository.syncPendingOperations()

        if (syncResult is Result.Error) {
            return syncResult
        }

        // Then fetch latest from server
        return when (val fetchResult = productRepository.fetchFromRemote()) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> fetchResult.copy(message = fetchResult.message)
            else -> Result.Error("Unexpected result")
        }
    }

    fun getPendingOperationsCount() = syncRepository.getPendingOperationsCount()
}