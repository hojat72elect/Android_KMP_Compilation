

package com.duckduckgo.request.filterer.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        SettingsEntity::class,
        RequestFiltererExceptionEntity::class,
    ],
)
abstract class RequestFiltererDatabase : RoomDatabase() {
    abstract fun requestFiltererDao(): RequestFiltererDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
