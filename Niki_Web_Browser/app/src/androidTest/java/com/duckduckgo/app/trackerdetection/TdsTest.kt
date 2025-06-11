

package com.duckduckgo.app.trackerdetection

import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.browser.R
import org.junit.Assert.assertEquals
import org.junit.Test

class TdsTest {

    /**
     * If this test fails is because you updated the internal tds json file without changing the default etag value in TrackerDataLoader.
     * Change the DEFAULT_ETAG value with the latest etag header from tds and then change the value of DEFAULT_TDS_HASH_VALUE below with the new one
     * to make the test pass.
     **/

    @Test
    fun whenInternalTdsFileChangesThenEtagValueChanges() {
        val tdsContent = InstrumentationRegistry.getInstrumentation().targetContext.resources.openRawResource(R.raw.tds)
            .bufferedReader().use { it.readText() }

        assertEquals(DEFAULT_TDS_HASH_VALUE, tdsContent.hashCode())
    }

    companion object {
        private const val DEFAULT_TDS_HASH_VALUE = -160289387
    }
}
