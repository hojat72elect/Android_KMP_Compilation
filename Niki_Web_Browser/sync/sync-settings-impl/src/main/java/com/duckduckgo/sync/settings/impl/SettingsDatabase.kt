

package com.duckduckgo.sync.settings.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        SettingsSyncMetadataEntity::class,
    ],
)
abstract class SettingsDatabase : RoomDatabase() {
    abstract fun settingsSyncDao(): SettingsSyncMetadataDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
