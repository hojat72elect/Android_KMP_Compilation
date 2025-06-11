

package com.duckduckgo.elementhiding.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        ElementHidingEntity::class,
    ],
)
abstract class ElementHidingDatabase : RoomDatabase() {
    abstract fun elementHidingDao(): ElementHidingDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
