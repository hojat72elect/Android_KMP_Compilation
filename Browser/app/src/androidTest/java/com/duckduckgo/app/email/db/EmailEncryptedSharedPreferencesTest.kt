

package com.duckduckgo.app.email.db

import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.test.CoroutineTestRule
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

@FlowPreview
class EmailEncryptedSharedPreferencesTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    private val mockPixel: Pixel = mock()
    lateinit var testee: EmailEncryptedSharedPreferences

    @Before
    fun before() {
        testee = EmailEncryptedSharedPreferences(InstrumentationRegistry.getInstrumentation().targetContext, mockPixel)
    }

    @Test
    fun whenNextAliasEqualsValueThenValueIsSentToNextAliasChannel() = runTest {
        testee.nextAlias = "test"

        assertEquals("test", testee.nextAlias)
    }

    @Test
    fun whenNextAliasEqualsNullThenNullIsSentToNextAliasChannel() = runTest {
        testee.nextAlias = null

        assertNull(testee.nextAlias)
    }
}
