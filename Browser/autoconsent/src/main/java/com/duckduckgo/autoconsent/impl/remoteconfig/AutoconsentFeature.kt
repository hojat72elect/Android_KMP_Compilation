package com.duckduckgo.autoconsent.impl.remoteconfig

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "autoconsent",
)
/**
 * This is the class that represents the autoconsent feature flags
 */
interface AutoconsentFeature {
    /**
     * @return `true` when the remote config has the global "voiceSearch" feature flag enabled
     * If the remote feature is not present defaults to `true`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun self(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun onByDefault(): Toggle
}
