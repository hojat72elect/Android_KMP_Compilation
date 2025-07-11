

package com.duckduckgo.app.global.api

import com.duckduckgo.adclick.impl.pixels.AdClickPixelName
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(scope = AppScope::class)
class PixelAdClickAttributionRemovalInterceptor @Inject constructor() : PixelParamRemovalPlugin {

    override fun names(): List<Pair<String, Set<PixelParameter>>> {
        return listOf(
            AdClickPixelName.AD_CLICK_DETECTED.pixelName to PixelParameter.removeAtb(),
            AdClickPixelName.AD_CLICK_ACTIVE.pixelName to PixelParameter.removeAtb(),
            AdClickPixelName.AD_CLICK_PAGELOADS_WITH_AD_ATTRIBUTION.pixelName to PixelParameter.removeAtb(),
        )
    }
}
