

package com.duckduckgo.downloads.store

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    exportSchema = true,
    version = 1,
    entities = [DownloadEntity::class],
)
abstract class DownloadsDatabase : RoomDatabase() {
    abstract fun downloadsDao(): DownloadsDao
}
