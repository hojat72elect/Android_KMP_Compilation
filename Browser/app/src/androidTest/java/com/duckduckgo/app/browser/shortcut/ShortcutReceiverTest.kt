

package com.duckduckgo.app.browser.shortcut

import android.content.Intent
import com.duckduckgo.app.pixels.AppPixelName
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.test.CoroutineTestRule
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ShortcutReceiverTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val mockPixel: Pixel = mock()
    private lateinit var testee: ShortcutReceiver

    @Before
    fun before() {
        testee = ShortcutReceiver()
        testee.pixel = mockPixel
        testee.dispatcher = coroutinesTestRule.testDispatcherProvider
        testee.appCoroutineScope = TestScope()
    }

    @Test
    fun whenIntentReceivedThenFireShortcutAddedPixel() {
        val intent = Intent()
        intent.putExtra(ShortcutBuilder.SHORTCUT_URL_ARG, "www.example.com")
        intent.putExtra(ShortcutBuilder.SHORTCUT_TITLE_ARG, "Title")
        testee.onShortcutAdded(null, intent)

        verify(mockPixel).fire(AppPixelName.SHORTCUT_ADDED)
    }
}
