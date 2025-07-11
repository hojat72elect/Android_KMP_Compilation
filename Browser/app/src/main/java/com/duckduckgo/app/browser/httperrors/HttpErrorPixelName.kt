

package com.duckduckgo.app.browser.httperrors

import com.duckduckgo.app.statistics.pixels.Pixel

enum class HttpErrorPixelName(override val pixelName: String) : Pixel.PixelName {
    WEBVIEW_RECEIVED_HTTP_ERROR_400_DAILY("m_webview_received_http_error_400_daily"),
    WEBVIEW_RECEIVED_HTTP_ERROR_4XX_DAILY("m_webview_received_http_error_4xx_daily"),
    WEBVIEW_RECEIVED_HTTP_ERROR_5XX_DAILY("m_webview_received_http_error_5xx_daily"),
}

object HttpErrorPixelParameters {
    const val HTTP_ERROR_CODE_COUNT = "count"
}
