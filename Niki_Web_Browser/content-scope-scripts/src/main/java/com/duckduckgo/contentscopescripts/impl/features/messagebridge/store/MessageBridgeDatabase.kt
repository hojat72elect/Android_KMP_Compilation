package com.duckduckgo.contentscopescripts.impl.features.messagebridge.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        MessageBridgeEntity::class,
    ],
)
abstract class MessageBridgeDatabase : RoomDatabase() {
    abstract fun messageBridgeDao(): MessageBridgeDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
