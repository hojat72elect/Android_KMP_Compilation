package com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        NavigatorInterfaceEntity::class,
    ],
)
abstract class NavigatorInterfaceDatabase : RoomDatabase() {
    abstract fun navigatorInterfaceDao(): NavigatorInterfaceDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
