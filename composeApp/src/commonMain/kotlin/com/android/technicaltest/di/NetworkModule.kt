package com.android.technicaltest.di

import com.android.technicaltest.data.remote.ApiClient.httpClient
import com.android.technicaltest.data.remote.ProductApi
import com.android.technicaltest.util.NetworkMonitor
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext


fun networkModule()  = module {

    single<CoroutineContext> { Dispatchers.Default }
    single { CoroutineScope(get()) }

    single<HttpClient> {
        httpClient
    }

    singleOf(::NetworkMonitor)
    singleOf(::ProductApi)
}