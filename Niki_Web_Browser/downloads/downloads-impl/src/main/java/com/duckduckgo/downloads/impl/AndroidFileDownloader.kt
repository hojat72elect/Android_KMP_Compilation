

package com.duckduckgo.downloads.impl

import android.webkit.URLUtil
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.downloads.api.DownloadFailReason
import com.duckduckgo.downloads.api.FileDownloader
import com.duckduckgo.downloads.api.FileDownloader.PendingFileDownload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AndroidFileDownloader constructor(
    private val dataUriDownloader: DataUriDownloader,
    private val callback: FileDownloadCallback,
    private val workManager: WorkManager,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : FileDownloader {

    override fun enqueueDownload(
        pending: PendingFileDownload,
    ) {
        when {
            pending.isNetworkUrl -> enqueueToWorker(pending)
            // We don't delegate the data URLs to the worker because you can't pass a lot of data through the [Data] class.
            pending.isDataUrl -> coroutineScope.launch(dispatcherProvider.io()) { dataUriDownloader.download(pending, callback) }
            else -> callback.onError(url = pending.url, reason = DownloadFailReason.UnsupportedUrlType)
        }
    }

    private fun enqueueToWorker(pending: PendingFileDownload) {
        OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setInputData(pending.toInputData())
            .build()
            .let {
                workManager.enqueue(it)
            }
    }
}

val PendingFileDownload.isDataUrl get() = URLUtil.isDataUrl(url)

val PendingFileDownload.isNetworkUrl get() = URLUtil.isNetworkUrl(url)
