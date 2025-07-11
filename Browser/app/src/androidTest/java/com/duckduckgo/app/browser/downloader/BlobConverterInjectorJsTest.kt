

package com.duckduckgo.app.browser.downloader

import android.webkit.WebView
import androidx.test.annotation.UiThreadTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.browser.R
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

class BlobConverterInjectorJsTest {
    lateinit var testee: BlobConverterInjectorJs
    private val blobUrl = "blob:https://example.com/283nasdho23jkasdAjd"
    private val contentType = "application/plain"

    @Before
    fun setup() {
        testee = BlobConverterInjectorJs()
    }

    @UiThreadTest
    @Test
    @SdkSuppress(minSdkVersion = 24)
    fun whenConvertBlobIntoDataUriAndDownloadThenInjectJsCode() {
        val jsToEvaluate = getJsToEvaluate().replace("%blobUrl%", blobUrl).replace("%contentType%", contentType)
        val webView = spy(WebView(InstrumentationRegistry.getInstrumentation().targetContext))

        testee.convertBlobIntoDataUriAndDownload(webView, blobUrl, contentType)

        verify(webView).evaluateJavascript(jsToEvaluate, null)
    }

    private fun getJsToEvaluate(): String {
        val js = InstrumentationRegistry.getInstrumentation().targetContext.resources.openRawResource(
            R.raw.blob_converter,
        )
            .bufferedReader()
            .use { it.readText() }
        return "javascript:$js"
    }
}
