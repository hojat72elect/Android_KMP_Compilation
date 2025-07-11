package com.duckduckgo.app.browser.urlextraction

import android.webkit.WebView
import androidx.test.annotation.UiThreadTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.common.test.CoroutineTestRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

class JsUrlExtractorTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    lateinit var testee: DOMUrlExtractor

    @UiThreadTest
    @Before
    fun setup() {
        testee = JsUrlExtractor()
    }

    @UiThreadTest
    @Test
    @SdkSuppress(minSdkVersion = 24)
    fun whenAddUrlExtractionThenJSInterfaceAdded() = runTest {
        val webView = spy(WebView(InstrumentationRegistry.getInstrumentation().targetContext))
        val onUrlExtracted = mock<(extractedUrl: String?) -> Unit>()
        testee.addUrlExtraction(webView, onUrlExtracted)
        verify(webView).addJavascriptInterface(
            any<UrlExtractionJavascriptInterface>(),
            eq(UrlExtractionJavascriptInterface.URL_EXTRACTION_JAVASCRIPT_INTERFACE_NAME),
        )
    }

    @UiThreadTest
    @Test
    @SdkSuppress(minSdkVersion = 24)
    fun whenPageStartedEventThenUrlExtractionJSInjected() = runTest {
        val webView = spy(WebView(InstrumentationRegistry.getInstrumentation().targetContext))
        testee.injectUrlExtractionJS(webView)
        verify(webView).evaluateJavascript(any(), anyOrNull())
    }
}
