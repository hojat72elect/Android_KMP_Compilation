

package com.duckduckgo.downloads.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import com.duckduckgo.downloads.store.DownloadStatus.STARTED

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val downloadId: Long,
    val downloadStatus: Int = STARTED,
    val fileName: String,
    val contentLength: Long = 0,
    val filePath: String,
    val createdAt: String = DatabaseDateFormatter.timestamp(),
)
