

package com.duckduckgo.feature.toggles.impl

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureToggle
import com.duckduckgo.feature.toggles.api.FeatureTogglesPlugin
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealFeatureToggleImpl @Inject constructor(private val featureTogglesPluginPoint: PluginPoint<FeatureTogglesPlugin>) :
    FeatureToggle {

    override fun isFeatureEnabled(
        featureName: String,
        defaultValue: Boolean,
    ): Boolean {
        featureTogglesPluginPoint.getPlugins().forEach { plugin ->
            plugin.isEnabled(featureName, defaultValue)?.let { return it }
        }

        throw IllegalArgumentException("Unknown feature: $featureName")
    }
}

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = FeatureTogglesPlugin::class,
)
@Suppress("unused")
private interface FeatureTogglesPluginPoint
