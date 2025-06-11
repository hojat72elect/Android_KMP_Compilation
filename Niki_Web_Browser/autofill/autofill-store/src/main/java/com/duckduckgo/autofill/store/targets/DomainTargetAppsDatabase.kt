

package com.duckduckgo.autofill.store.targets

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        DomainTargetAppEntity::class,
    ],
)
abstract class DomainTargetAppsDatabase : RoomDatabase() {
    abstract fun domainTargetAppDao(): DomainTargetAppDao

    companion object {
        val ALL_MIGRATIONS = emptyArray<Migration>()
    }
}
