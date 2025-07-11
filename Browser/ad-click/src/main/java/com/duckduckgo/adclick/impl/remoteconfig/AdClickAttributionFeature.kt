package com.duckduckgo.adclick.impl.remoteconfig

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "adClickAttribution",
    settingsStore = AdClickAttributionFeatureSettingsStore::class,
)
/**
 * This is the class that represents the adClickAttribution feature flags.
 */
interface AdClickAttributionFeature {
    /**
     * @return `true` when the remote config has the global "adClickAttribution" feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    /**
     * @return `true` when the remote config has the global "persistExemptions" adClickAttribution sub-feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun persistExemptions(): Toggle
}
