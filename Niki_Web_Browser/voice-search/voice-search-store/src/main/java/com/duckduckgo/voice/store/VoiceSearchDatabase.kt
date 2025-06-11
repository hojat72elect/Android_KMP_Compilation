

package com.duckduckgo.voice.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 2,
    entities = [
        ManufacturerEntity::class,
        LocaleEntity::class,
        MinVersionEntity::class,
    ],
)
abstract class VoiceSearchDatabase : RoomDatabase() {
    abstract fun voiceSearchDao(): VoiceSearchDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
