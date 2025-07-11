

package com.duckduckgo.runtimechecks.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        RuntimeChecksEntity::class,
    ],
)
abstract class RuntimeChecksDatabase : RoomDatabase() {
    abstract fun runtimeChecksDao(): RuntimeChecksDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
