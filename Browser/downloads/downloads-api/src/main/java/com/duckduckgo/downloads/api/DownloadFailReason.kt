

package com.duckduckgo.downloads.api

/** Each failed download has a specific reason represented by a [DownloadFailReason] object. */
sealed class DownloadFailReason {

    object UnsupportedUrlType : DownloadFailReason()
    object Other : DownloadFailReason()
    object DataUriParseException : DownloadFailReason()
    object ConnectionRefused : DownloadFailReason()
}
