

package com.duckduckgo.networkprotection.internal.feature

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "netpInternal",
    // we define store because we need something multi-process
    toggleStore = NetPInternalFeatureToggleStore::class,
)
interface NetPInternalFeatureToggles {
    @Toggle.DefaultValue(defaultValue = DefaultFeatureValue.TRUE)
    fun self(): Toggle

    @Toggle.DefaultValue(defaultValue = DefaultFeatureValue.FALSE)
    fun excludeSystemApps(): Toggle

    @Toggle.DefaultValue(defaultValue = DefaultFeatureValue.FALSE)
    fun enablePcapRecording(): Toggle

    @Toggle.DefaultValue(defaultValue = DefaultFeatureValue.FALSE)
    fun useVpnStagingEnvironment(): Toggle
}
