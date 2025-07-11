

package com.duckduckgo.installation.impl.installer.aura

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "auraExperiment",
)
interface AuraExperimentFeature {

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle
}
