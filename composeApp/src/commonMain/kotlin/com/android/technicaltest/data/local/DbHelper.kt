package com.android.technicaltest.data.local

import com.android.technicaltest.database.AppDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DbHelper(
    private val driverFactory: DatabaseFactory
) {

    private var db: AppDatabase? = null
    private val mutex = Mutex()

    suspend fun <Result : Any?> withDatabase(block: suspend (AppDatabase) -> Result) =
        mutex.withLock {
            if (db == null) {
                db = createDb(driverFactory)
            }

            return@withLock block(db!!)
        }

    private suspend fun createDb(driverFactory: DatabaseFactory): AppDatabase {
        return AppDatabase(
            driver = driverFactory.createDriver(),
        )
    }
}