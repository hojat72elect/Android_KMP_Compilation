

package com.duckduckgo.app.browser.applinks

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "externalAppIntentFlags",
)

// A safeguard for this fix: https://app.asana.com/0/0/1207374732742336/1207383486029585/f
interface ExternalAppIntentFlagsFeature {

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun self(): Toggle
}
