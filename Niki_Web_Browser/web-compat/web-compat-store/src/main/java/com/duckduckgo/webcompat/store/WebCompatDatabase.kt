

package com.duckduckgo.webcompat.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        WebCompatEntity::class,
    ],
)
abstract class WebCompatDatabase : RoomDatabase() {
    abstract fun webCompatDao(): WebCompatDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
