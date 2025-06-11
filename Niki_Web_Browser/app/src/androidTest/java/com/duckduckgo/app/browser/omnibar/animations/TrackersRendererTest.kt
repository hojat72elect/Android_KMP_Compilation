

package com.duckduckgo.app.browser.omnibar.animations

import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.browser.R
import org.junit.Assert.assertEquals
import org.junit.Test

class TrackersRendererTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val testee = TrackersRenderer()

    @Test
    fun whenNetworkNameMatchesLogoIconThenResourceIsReturned() {
        val resource = testee.networkLogoIcon(context, "outbrain")
        assertEquals(R.drawable.network_logo_outbrain, resource)
    }

    @Test
    fun whenNetworkNameSansSpecialCharactersAndWithUnderscoresForSpacesMatchesLogoIconThenResourceIsReturned() {
        val resource = testee.networkLogoIcon(context, "Amazon Technologies, Inc.")
        assertEquals(R.drawable.network_logo_amazon_technologies_inc, resource)
    }
}
