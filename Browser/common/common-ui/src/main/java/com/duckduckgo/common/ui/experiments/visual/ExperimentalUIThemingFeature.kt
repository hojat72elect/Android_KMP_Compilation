

package com.duckduckgo.common.ui.experiments.visual

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "experimentalUITheming",
)
interface ExperimentalUIThemingFeature {
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun duckAIPoCFeature(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun visualUpdatesFeature(): Toggle
}
