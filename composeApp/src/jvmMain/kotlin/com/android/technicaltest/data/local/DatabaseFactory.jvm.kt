package com.android.technicaltest.data.local

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.awaitMigrate
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.android.technicaltest.database.AppDatabase
import java.io.File
import kotlinx.coroutines.runBlocking

class DesktopDatabaseFactory : DatabaseFactory {
    override fun createDriver(): SqlDriver {
        val databasePath = File(System.getProperty("user.home"), ".productsync")
        databasePath.mkdirs()

        val databaseFile = File(databasePath, "productsync.db")
        val databaseAlreadyExists = databaseFile.exists()
        val driver = JdbcSqliteDriver("jdbc:sqlite:${databaseFile.absolutePath}")

        initialiseSchema(driver, databaseAlreadyExists)

        return driver
    }

    private fun initialiseSchema(driver: SqlDriver, databaseAlreadyExists: Boolean) {
        val targetVersion = AppDatabase.Schema.version

        val currentVersion = if (databaseAlreadyExists) {
            currentSchemaVersion(driver)
        } else {
            null
        }

        when {
            currentVersion == null -> runBlocking {
                AppDatabase.Schema.awaitCreate(driver)
            }

            currentVersion > targetVersion -> error(
                "Database version ($currentVersion) is newer than application schema ($targetVersion)."
            )

            else -> runBlocking {
                if (currentVersion < targetVersion) {
                    AppDatabase.Schema.awaitMigrate(driver, currentVersion, targetVersion)
                }

                // Always execute the CREATE statements to backfill any tables that were added
                // without bumping the schema version (all CREATE statements use IF NOT EXISTS).
                AppDatabase.Schema.awaitCreate(driver)
            }
        }
    }

    private fun currentSchemaVersion(driver: SqlDriver): Long? {
        return try {
            driver.executeQuery(
                identifier = null,
                sql = "SELECT value FROM sqldelight_metadata WHERE name = 'schema_version'",
                mapper = { cursor ->
                    val hasRow = cursor.next().value
                    QueryResult.Value(if (hasRow) cursor.getLong(0) else null)
                },
                parameters = 0,
                binders = null,
            ).value
        } catch (_: Exception) {
            null
        }
    }

}
