

package com.duckduckgo.app.trackerdetection.blocklist

import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.dashboard.api.PrivacyProtectionTogglePlugin
import com.duckduckgo.privacy.dashboard.api.PrivacyToggleOrigin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class BlockListPrivacyTogglePlugin @Inject constructor(
    private val blockListPixelsPlugin: BlockListPixelsPlugin,
    private val pixel: Pixel,
) : PrivacyProtectionTogglePlugin {

    override suspend fun onToggleOn(origin: PrivacyToggleOrigin) {
        // NOOP
    }

    override suspend fun onToggleOff(origin: PrivacyToggleOrigin) {
        if (origin == PrivacyToggleOrigin.DASHBOARD) {
            blockListPixelsPlugin.getPrivacyToggleUsed()?.getPixelDefinitions()?.forEach {
                pixel.fire(it.pixelName, it.params)
            }
        }
    }
}
