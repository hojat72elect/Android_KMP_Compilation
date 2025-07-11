

package com.duckduckgo.app.browser

import androidx.test.annotation.UiThreadTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertFalse
import org.junit.Test

class DuckDuckGoWebViewTest {

    @Test
    @UiThreadTest
    fun whenWebViewInitialisedThenSafeBrowsingDisabled() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val testee = DuckDuckGoWebView(context)
        assertFalse(testee.settings.safeBrowsingEnabled)
    }
}
