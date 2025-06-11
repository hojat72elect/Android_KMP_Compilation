

package com.duckduckgo.widget

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class VoiceSearchWidgetUpdaterReceiverTest {

    private val testee = VoiceSearchWidgetUpdaterReceiver()
    private val updater: WidgetUpdater = mock()

    @Before
    fun setup() {
        testee.widgetUpdater = updater
    }

    @Test
    fun whenProcessIntentThenShouldUpdateWidgets() {
        val intent = Intent().apply {
            action = Intent.ACTION_LOCALE_CHANGED
        }
        val context = ApplicationProvider.getApplicationContext<Context>()

        testee.processIntent(context, intent)

        verify(updater).updateWidgets(any())
    }
}
