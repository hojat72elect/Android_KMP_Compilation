

package com.duckduckgo.app.browser.webview

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(
    scope = ActivityScope::class,
    replaces = [RealWebContentDebugging::class],
)
class InternalWebContentDebugging @Inject constructor(
    private val webContentDebuggingFeature: WebContentDebuggingFeature,
) : WebContentDebugging {
    override fun isEnabled(): Boolean {
        return webContentDebuggingFeature.webContentDebugging().isEnabled()
    }
}

@ContributesRemoteFeature(
    scope = ActivityScope::class,
    featureName = "InternalWebContentDebuggingFlag",
)
interface WebContentDebuggingFeature {
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun webContentDebugging(): Toggle
}
