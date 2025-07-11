

package com.duckduckgo.downloads.api

import android.content.Context
import com.duckduckgo.downloads.api.FileDownloader.PendingFileDownload
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import kotlinx.coroutines.flow.Flow

/** Interface containing download callbacks. */
interface DownloadStateListener {
    /**
     * Data stream that sequentially emits commands of type [DownloadCommand].
     */
    fun commands(): Flow<DownloadCommand>
}

interface DownloadConfirmationDialogListener {
    fun continueDownload(pendingFileDownload: PendingFileDownload)
    fun cancelDownload()
}

interface DownloadConfirmation {
    fun instance(pendingDownload: PendingFileDownload): BottomSheetDialogFragment
}

interface DownloadsFileActions {
    fun openFile(applicationContext: Context, file: File): Boolean
    fun shareFile(applicationContext: Context, file: File): Boolean
}
