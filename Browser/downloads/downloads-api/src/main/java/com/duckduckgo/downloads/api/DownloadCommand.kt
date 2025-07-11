

package com.duckduckgo.downloads.api

import androidx.annotation.StringRes

/** Specific download commands used to display messages during various download stages. */
sealed class DownloadCommand(@StringRes val messageId: Int) {
    class ShowDownloadStartedMessage(
        @StringRes messageId: Int,
        val fileName: String,
    ) : DownloadCommand(messageId)
    class ShowDownloadSuccessMessage(
        @StringRes messageId: Int,
        val fileName: String,
        val filePath: String,
        val mimeType: String? = null,
    ) : DownloadCommand(messageId)
    class ShowDownloadFailedMessage(
        @StringRes messageId: Int,
    ) : DownloadCommand(messageId)
}

const val DOWNLOAD_SNACKBAR_LENGTH = 750
const val DOWNLOAD_SNACKBAR_DELAY = 1500L
