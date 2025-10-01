package com.android.technicaltest.data.local

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.android.technicaltest.database.AppDatabase
import kotlin.io.path.createTempDirectory
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class DesktopDatabaseFactoryTest {
    @Test
    fun createsSchemaForNewDatabase() = withTempUserHome { factory, _ ->
        factory.createDriver().useDriver { driver ->
            assertTrue(hasTable(driver, "ProductEntity"))
            assertTrue(hasTable(driver, "QueuedOperationEntity"))
        }
    }

    @Test
    fun backfillsTablesWhenSchemaVersionMatches() = withTempUserHome { factory, databaseFile ->
        databaseFile.parentFile?.mkdirs()

        JdbcSqliteDriver("jdbc:sqlite:${'$'}{databaseFile.absolutePath}").useDriver { driver ->
            driver.execute(
                identifier = null,
                sql = "CREATE TABLE IF NOT EXISTS sqldelight_metadata (name TEXT PRIMARY KEY NOT NULL, value TEXT NOT NULL)",
                parameters = 0,
                binders = null,
            ).value
            driver.execute(
                identifier = null,
                sql = "INSERT OR REPLACE INTO sqldelight_metadata(name, value) VALUES('schema_version', ?)",
                parameters = 1,
            ) {
                bindLong(0, AppDatabase.Schema.version.toLong())
            }.value
        }

        factory.createDriver().useDriver { driver ->
            assertTrue(hasTable(driver, "ProductEntity"))
            assertTrue(hasTable(driver, "QueuedOperationEntity"))
        }
    }

    private inline fun withTempUserHome(block: (DesktopDatabaseFactory, File) -> Unit) {
        val tempDir = createTempDirectory(prefix = "dbfactory-test").toFile()
        val originalUserHome = System.getProperty("user.home")
        System.setProperty("user.home", tempDir.absolutePath)
        val databaseFile = File(File(tempDir, ".productsync"), "productsync.db")

        try {
            block(DesktopDatabaseFactory(), databaseFile)
        } finally {
            if (originalUserHome == null) {
                System.clearProperty("user.home")
            } else {
                System.setProperty("user.home", originalUserHome)
            }

            tempDir.deleteRecursively()
        }
    }

    private fun hasTable(driver: SqlDriver, tableName: String): Boolean {
        return driver.executeQuery(
            identifier = null,
            sql = "SELECT name FROM sqlite_master WHERE type='table' AND name = ?",
            mapper = { cursor ->
                QueryResult.Value(cursor.next().value)
            },
            parameters = 1,
        ) {
            bindString(0, tableName)
        }.value
    }

    private inline fun <T : SqlDriver, R> T.useDriver(block: (T) -> R): R {
        try {
            return block(this)
        } finally {
            close()
        }
    }
}
