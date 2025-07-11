

package com.duckduckgo.duckchat.impl.feature

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "aiChat",
)
/**
 * This is the class that represents the aiChat feature flags
 */
interface DuckChatFeature {
    /**
     * @return `true` when the remote config has the global "aiChat" feature flag enabled
     * If the remote feature is not present defaults to `false`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle
}
