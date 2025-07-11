

package com.duckduckgo.feature.toggles.impl

import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.METRICS_PIXEL_PREFIX
import com.duckduckgo.feature.toggles.impl.FeatureTogglesPixelName.EXPERIMENT_ENROLLMENT
import com.squareup.anvil.annotations.ContributesMultibinding

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PixelParamRemovalPlugin::class,
)
object MetricPixelRemovalInterceptor : PixelParamRemovalPlugin {
    override fun names(): List<Pair<String, Set<PixelParameter>>> {
        return listOf(
            METRICS_PIXEL_PREFIX to PixelParameter.removeAll(),
            EXPERIMENT_ENROLLMENT.pixelName to PixelParameter.removeAll(),
        )
    }
}
