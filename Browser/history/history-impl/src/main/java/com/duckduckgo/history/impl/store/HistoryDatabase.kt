

package com.duckduckgo.history.impl.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        HistoryEntryEntity::class,
        VisitEntity::class,
    ],
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
