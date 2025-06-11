

package com.duckduckgo.downloads.impl.pixels

import com.duckduckgo.app.statistics.pixels.Pixel

internal enum class DownloadsPixelName(override val pixelName: String) : Pixel.PixelName {
    DOWNLOAD_REQUEST_STARTED("m_download_request_started"),
    DOWNLOAD_REQUEST_SUCCEEDED("m_download_request_succeeded"),
    DOWNLOAD_REQUEST_FAILED("m_download_request_failed"),
    DOWNLOAD_REQUEST_CANCELLED("m_download_request_cancelled"),
    DOWNLOAD_REQUEST_CANCELLED_BY_USER("m_download_request_cancelled_by_user"),
    DOWNLOAD_REQUEST_RETRIED("m_download_request_retried"),
}
