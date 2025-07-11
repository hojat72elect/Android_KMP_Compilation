

package com.duckduckgo.malicioussiteprotection.impl

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "maliciousSiteProtection",
)
/**
 * This is the class that represents the maliciousSiteProtection feature flags
 */
interface MaliciousSiteProtectionFeature {
    /**
     * @return `true` when the remote config has the global "maliciousSiteProtection" feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.InternalAlwaysEnabled
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    @Toggle.InternalAlwaysEnabled
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun visibleAndOnByDefault(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun canUpdateDatasets(): Toggle

    @Toggle.InternalAlwaysEnabled
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun scamProtection(): Toggle
}
