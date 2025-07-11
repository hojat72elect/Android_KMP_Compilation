

package com.duckduckgo.downloads.api

import androidx.annotation.AnyThread
import java.io.File

interface FileDownloadNotificationManager {
    /**
     * Show an "in progress" notification for the file being downloaded.
     * It can be called multiple times with the [progress] of the download, and receives the [downloadId] and the [filename].
     * Although it is safe to call this method from any thread, if called too frequently, it is recommended to call it on a background thread.
     */
    @AnyThread
    fun showDownloadInProgressNotification(downloadId: Long, filename: String, progress: Int = 0)

    /**
     * Call this method to show the "download complete" notification.
     * Takes as parameters the [downloadId], the downloaded [file] and optionally the file [mimeType]
     * Safe to call from any thread.
     */
    @AnyThread
    fun showDownloadFinishedNotification(downloadId: Long, file: File, mimeType: String?)

    /**
     * Call this method to show the "download failed" notification.
     * Takes as parameters the [downloadId] and the download [url].
     * Safe to call from any thread.
     */
    @AnyThread
    fun showDownloadFailedNotification(downloadId: Long, url: String?)

    /**
     * Call this method to show the "download cancelled" notification.
     * Takes as parameter the [downloadId].
     * Safe to call from any thread.
     */
    @AnyThread
    fun cancelDownloadFileNotification(downloadId: Long)
}
