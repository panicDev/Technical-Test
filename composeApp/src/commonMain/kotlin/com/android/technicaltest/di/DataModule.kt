package com.android.technicaltest.di

import app.cash.sqldelight.db.SqlDriver
import com.android.technicaltest.data.local.DatabaseFactory
import com.android.technicaltest.data.local.DbHelper
import com.android.technicaltest.data.local.ProductDatabase
import com.android.technicaltest.data.local.QueueDatabase
import com.android.technicaltest.data.repository.ProductRepository
import com.android.technicaltest.data.repository.SyncRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun dataModule()  = module {
    single<SqlDriver> { get<DatabaseFactory>().createDriver() }
    singleOf(::ProductDatabase)
    singleOf(::QueueDatabase)
    single { DbHelper(get()) }
    singleOf(::ProductRepository)
    singleOf(::SyncRepository)
}