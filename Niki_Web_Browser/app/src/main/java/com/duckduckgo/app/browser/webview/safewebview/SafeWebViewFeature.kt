

package com.duckduckgo.app.browser.webview.safewebview

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "safeWebView",
)

interface SafeWebViewFeature {

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun self(): Toggle
}
