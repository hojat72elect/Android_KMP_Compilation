

package com.duckduckgo.app.browser.navigation

import android.content.Context
import android.webkit.WebBackForwardList
import android.webkit.WebView
import androidx.test.annotation.UiThreadTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class WebViewBackForwardListSafeExtractorTest {

    private val context = getInstrumentation().targetContext

    @UiThreadTest
    @Test
    fun whenCopyBackForwardListCalledAndExceptionThrownThenNavigationListIsNull() {
        val testWebView = TestWebViewThrowsNullPointerException(context)
        assertNull(testWebView.safeCopyBackForwardList())
    }

    @UiThreadTest
    @Test
    fun whenCopyBackForwardListCalledAndNoExceptionThrownThenNavigationListIsNotNull() {
        val testWebView = WebView(context)
        assertNotNull(testWebView.safeCopyBackForwardList())
    }
}

class TestWebViewThrowsNullPointerException(context: Context) : WebView(context) {

    override fun copyBackForwardList(): WebBackForwardList {
        throw NullPointerException("Deliberate")
    }
}
