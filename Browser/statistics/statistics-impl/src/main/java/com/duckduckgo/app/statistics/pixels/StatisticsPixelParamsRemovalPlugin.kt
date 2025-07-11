

package com.duckduckgo.app.statistics.pixels

import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PixelParamRemovalPlugin::class,
)
object StatisticsPixelParamsRemovalPlugin : PixelParamRemovalPlugin {
    override fun names(): List<Pair<String, Set<PixelParameter>>> {
        return listOf(
            StatisticsPixelName.BROWSER_DAILY_ACTIVE_FEATURE_STATE.pixelName to PixelParameter.removeAll(),
            StatisticsPixelName.RETENTION_SEGMENTS.pixelName to PixelParameter.removeAll(),
        )
    }
}
