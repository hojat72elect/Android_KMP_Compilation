

package com.duckduckgo.app.fire

import android.database.sqlite.SQLiteDatabase
import com.duckduckgo.common.utils.DefaultDispatcherProvider
import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import timber.log.Timber

interface DatabaseCleaner {
    suspend fun cleanDatabase(databasePath: String): Boolean
    suspend fun changeJournalModeToDelete(databasePath: String): Boolean
}

class DatabaseCleanerHelper(private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()) : DatabaseCleaner {

    override suspend fun cleanDatabase(databasePath: String): Boolean {
        return executeCommand("VACUUM", databasePath)
    }

    override suspend fun changeJournalModeToDelete(databasePath: String): Boolean {
        return executeCommand("PRAGMA journal_mode=DELETE", databasePath)
    }

    private suspend fun executeCommand(
        command: String,
        databasePath: String,
    ): Boolean {
        return withContext(dispatcherProvider.io()) {
            if (databasePath.isNotEmpty()) {
                var commandExecuted = false
                openReadableDatabase(databasePath)?.use { db ->
                    try {
                        db.rawQuery(command, null).use { cursor -> cursor.moveToFirst() }
                        commandExecuted = true
                    } catch (exception: Exception) {
                        Timber.e(exception)
                    }
                }
                return@withContext commandExecuted
            }
            return@withContext false
        }
    }

    private fun openReadableDatabase(databasePath: String): SQLiteDatabase? {
        return try {
            SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE, null)
        } catch (exception: Exception) {
            null
        }
    }
}
