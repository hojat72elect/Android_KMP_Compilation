package com.duckduckgo.breakagereporting.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        BreakageReportingEntity::class,
    ],
)
abstract class BreakageReportingDatabase : RoomDatabase() {
    abstract fun breakageReportingDao(): BreakageReportingDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
