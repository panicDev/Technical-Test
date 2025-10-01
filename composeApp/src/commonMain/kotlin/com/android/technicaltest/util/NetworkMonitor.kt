package com.android.technicaltest.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.InetSocketAddress
import java.net.Socket

class NetworkMonitor {
    fun observeConnectivity(): Flow<Boolean> = flow {
        while (true) {
            val isConnected = isNetworkAvailable()
            emit(isConnected)
            delay(5000) // Check every 5 seconds
        }
    }

    private fun isNetworkAvailable(): Boolean {
        return try {
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            socket.connect(socketAddress, 2000)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun checkConnection(): Boolean {
        return isNetworkAvailable()
    }
}