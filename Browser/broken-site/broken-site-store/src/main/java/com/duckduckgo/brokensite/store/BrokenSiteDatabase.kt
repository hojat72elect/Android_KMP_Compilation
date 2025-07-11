

package com.duckduckgo.brokensite.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        BrokenSiteLastSentReportEntity::class,
    ],
)
abstract class BrokenSiteDatabase : RoomDatabase() {
    abstract fun brokenSiteDao(): BrokenSiteDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
