package com.duckduckgo.app.browser.urlextraction

import android.webkit.JavascriptInterface
import timber.log.Timber

class UrlExtractionJavascriptInterface(private val onUrlExtracted: (extractedUrl: String?) -> Unit) {

    @JavascriptInterface
    fun urlExtracted(extractedUrl: String?) {
        onUrlExtracted(extractedUrl)
    }

    @JavascriptInterface
    fun log(message: String?) {
        Timber.d("AMP link detection: $message")
    }

    companion object {
        // Interface name used inside url_extraction.js
        const val URL_EXTRACTION_JAVASCRIPT_INTERFACE_NAME = "UrlExtraction"
    }
}
