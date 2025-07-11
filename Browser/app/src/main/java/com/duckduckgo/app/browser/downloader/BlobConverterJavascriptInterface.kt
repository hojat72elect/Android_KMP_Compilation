

package com.duckduckgo.app.browser.downloader

import android.webkit.JavascriptInterface

class BlobConverterJavascriptInterface(private val onBlobConverted: (url: String, mimeType: String) -> Unit) {

    @JavascriptInterface
    fun convertBlobToDataUri(
        dataUrl: String,
        contentType: String,
    ) {
        onBlobConverted(dataUrl, contentType)
    }

    companion object {
        const val JAVASCRIPT_INTERFACE_NAME = "BlobConverter"
    }
}
