

package com.duckduckgo.app.anr

import com.duckduckgo.app.anr.CrashPixel.APPLICATION_CRASH_GLOBAL_VERIFIED_INSTALL
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PixelParamRemovalPlugin::class,
)
class VerifiedCrashPixelDataCleaner @Inject constructor() : PixelParamRemovalPlugin {
    override fun names(): List<Pair<String, Set<PixelParameter>>> {
        return listOf(
            APPLICATION_CRASH_GLOBAL_VERIFIED_INSTALL.pixelName to PixelParameter.removeAtb(),
        )
    }
}
