

package com.duckduckgo.duckplayer.impl

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "duckPlayer",
    settingsStore = DuckPlayerFatureSettingsStore::class,
)
/**
 * This is the class that represents the duckPlayer feature flags
 */
interface DuckPlayerFeature {
    /**
     * @return `true` when the remote config has the global "duckPlayer" feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    /**
     * @return `true` when the remote config has the "enableDuckPlayer" feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun enableDuckPlayer(): Toggle

    /**
     * @return `true` when the remote config has the "openInNewTab" feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun openInNewTab(): Toggle

    /**
     * @return `true` when the remote config has the "customError" feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun customError(): Toggle
}
