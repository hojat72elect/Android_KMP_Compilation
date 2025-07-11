

package com.duckduckgo.app.fire

import androidx.core.content.edit
import com.duckduckgo.app.fire.UnsentForgetAllPixelStoreSharedPreferences.Companion.FILENAME
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UnsentForgetAllPixelStoreSharedPreferencesTest {

    private lateinit var testee: UnsentForgetAllPixelStoreSharedPreferences

    @Before
    fun setup() {
        val context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences(FILENAME, 0).edit { clear() }
        testee = UnsentForgetAllPixelStoreSharedPreferences(context)
    }

    @Test
    fun whenFirstInitialisedThenPendingCountIs0() {
        assertEquals(0, testee.pendingPixelCountClearData)
    }

    @Test
    fun whenIncrementedOneThenValueIncrementsTo1() {
        testee.incrementCount()
        assertEquals(1, testee.pendingPixelCountClearData)
    }

    @Test
    fun whenIncrementedManyTimesThenIncrementsAsExpected() {
        testee.incrementCount()
        testee.incrementCount()
        testee.incrementCount()
        assertEquals(3, testee.pendingPixelCountClearData)
    }

    @Test
    fun whenFirstInitialisedThenLastClearTimestampIs0() {
        assertEquals(0, testee.lastClearTimestamp)
    }

    @Test
    fun whenIncrementedThenTimestampUpdated() {
        testee.incrementCount()
        assertTrue(testee.lastClearTimestamp > 0)
    }

    @Test
    fun whenResetWhenAlready0ThenCountIs0() {
        testee.resetCount()
        assertEquals(0, testee.pendingPixelCountClearData)
    }

    @Test
    fun whenResetFromAbove0ThenCountIs0() {
        testee.incrementCount()
        testee.resetCount()
        assertEquals(0, testee.pendingPixelCountClearData)
    }
}
