

package com.duckduckgo.app.browser.duckchat

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "aiChatQueryDetectionFeature",
)

interface AIChatQueryDetectionFeature {

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun self(): Toggle
}
