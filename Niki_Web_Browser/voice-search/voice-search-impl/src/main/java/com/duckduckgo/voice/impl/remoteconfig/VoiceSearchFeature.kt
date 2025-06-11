

package com.duckduckgo.voice.impl.remoteconfig

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "voiceSearch",
    settingsStore = VoiceSearchFeatureSettingStore::class,
)
/**
 * This is the class that represents the voiceSearch feature flags
 */
interface VoiceSearchFeature {
    /**
     * @return `true` when the remote config has the global "voiceSearch" feature flag enabled
     * If the remote feature is not present defaults to `true`
     */
    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun self(): Toggle
}
