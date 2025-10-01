package com.android.technicaltest.data.local

import app.cash.sqldelight.db.SqlDriver

interface DatabaseFactory {
    fun createDriver(): SqlDriver
}