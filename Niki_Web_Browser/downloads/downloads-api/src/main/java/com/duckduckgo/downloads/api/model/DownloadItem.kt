

package com.duckduckgo.downloads.api.model

/** Public data class for a downloaded file. */
data class DownloadItem(
    val downloadId: Long,
    val downloadStatus: Int,
    val fileName: String,
    val contentLength: Long,
    val createdAt: String,
    val filePath: String,
)
