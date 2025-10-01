package com.android.technicaltest.di

import com.android.technicaltest.data.local.DatabaseFactory
import com.android.technicaltest.data.local.DesktopDatabaseFactory
import org.koin.dsl.module
val jvmModules = module {
    single<DatabaseFactory> { DesktopDatabaseFactory() }
}

fun initKoinJvm() = initKoin(additionalModules = listOf(jvmModules))