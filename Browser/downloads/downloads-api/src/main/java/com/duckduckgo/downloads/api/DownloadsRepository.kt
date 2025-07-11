

package com.duckduckgo.downloads.api

import com.duckduckgo.downloads.api.model.DownloadItem
import kotlinx.coroutines.flow.Flow

interface DownloadsRepository {
    suspend fun insert(downloadItem: DownloadItem): Long
    suspend fun insertAll(downloadItems: List<DownloadItem>)
    suspend fun update(downloadId: Long, downloadStatus: Int, contentLength: Long)
    suspend fun update(fileName: String, downloadStatus: Int, contentLength: Long)
    suspend fun delete(downloadId: Long)
    suspend fun delete(downloadIdList: List<Long>)
    suspend fun deleteAll()
    suspend fun getDownloads(): List<DownloadItem>
    suspend fun getDownloadItem(downloadId: Long): DownloadItem?
    fun getDownloadsAsFlow(): Flow<List<DownloadItem>>
}
