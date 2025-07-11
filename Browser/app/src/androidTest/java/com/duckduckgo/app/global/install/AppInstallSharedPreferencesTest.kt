

package com.duckduckgo.app.global.install

import android.content.Context
import androidx.core.content.edit
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AppInstallSharedPreferencesTest {

    private lateinit var testee: AppInstallSharedPreferences

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        context.getSharedPreferences(AppInstallSharedPreferences.FILENAME, Context.MODE_PRIVATE).edit { clear() }
        testee = AppInstallSharedPreferences(context)
    }

    @Test
    fun whenInitializedThenInstallTimestampNotYetRecorded() {
        assertFalse(testee.hasInstallTimestampRecorded())
    }

    @Test
    fun whenInstallTimestampRecordedThenTimestampMarkedAsAvailable() {
        val timestamp = 1L
        testee.installTimestamp = timestamp
        assertTrue(testee.hasInstallTimestampRecorded())
    }

    @Test
    fun whenTimestampRecordedThenSameTimestampRetrieved() {
        val timestamp = 1L
        testee.installTimestamp = timestamp
        assertEquals(timestamp, testee.installTimestamp)
    }
}
