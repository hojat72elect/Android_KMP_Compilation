

package com.duckduckgo.app.browser.downloader

import android.content.Context
import android.webkit.WebView
import androidx.annotation.UiThread
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.downloader.BlobConverterJavascriptInterface.Companion.JAVASCRIPT_INTERFACE_NAME

interface BlobConverterInjector {
    fun addJsInterface(
        webView: WebView,
        onBlobTransformed: (url: String, mimeType: String) -> Unit,
    )

    fun convertBlobIntoDataUriAndDownload(
        webView: WebView,
        blobUrl: String,
        contentType: String?,
    )
}

class BlobConverterInjectorJs : BlobConverterInjector {
    private val javaScriptInjector = JavaScriptInjector()

    override fun addJsInterface(
        webView: WebView,
        onBlobTransformed: (url: String, mimeType: String) -> Unit,
    ) {
        webView.addJavascriptInterface(BlobConverterJavascriptInterface(onBlobTransformed), JAVASCRIPT_INTERFACE_NAME)
    }

    @UiThread
    override fun convertBlobIntoDataUriAndDownload(
        webView: WebView,
        blobUrl: String,
        contentType: String?,
    ) {
        webView.evaluateJavascript("javascript:${javaScriptInjector.getFunctionsJS(webView.context, blobUrl, contentType)}", null)
    }

    private class JavaScriptInjector {
        private lateinit var functions: String

        fun getFunctionsJS(
            context: Context,
            blobUrl: String,
            contentType: String?,
        ): String {
            if (!this::functions.isInitialized) {
                functions = context.resources.openRawResource(R.raw.blob_converter).bufferedReader().use { it.readText() }
            }
            return functions.replace("%blobUrl%", blobUrl).replace("%contentType%", contentType.orEmpty())
        }
    }
}
