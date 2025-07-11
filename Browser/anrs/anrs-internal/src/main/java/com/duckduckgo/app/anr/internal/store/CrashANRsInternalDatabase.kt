

package com.duckduckgo.app.anr.internal.store

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        AnrInternalEntity::class,
        CrashInternalEntity::class,
    ],
)
abstract class CrashANRsInternalDatabase : RoomDatabase() {
    abstract fun anrDao(): InternalANRDao
    abstract fun crashDao(): InternalCrashDao

    companion object {
        val ALL_MIGRATIONS = emptyArray<Migration>()
    }
}

@Entity(tableName = "anr_events")
data class AnrInternalEntity(
    @PrimaryKey val hash: String,
    val message: String,
    val name: String,
    val file: String?,
    val lineNumber: Int,
    val stackTrace: String,
    val timestamp: String,
    val webView: String,
    val customTab: Boolean,
)

@Entity(tableName = "crash_events")
data class CrashInternalEntity(
    @PrimaryKey val hash: String,
    val shortName: String,
    val processName: String,
    val message: String,
    val stackTrace: String,
    val version: String,
    val timestamp: String,
    val webView: String,
    val customTab: Boolean,
)
