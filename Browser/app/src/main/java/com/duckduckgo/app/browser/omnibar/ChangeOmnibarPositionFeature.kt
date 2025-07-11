

package com.duckduckgo.app.browser.omnibar

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "changeOmnibarPosition",
)
interface ChangeOmnibarPositionFeature {
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @Toggle.InternalAlwaysEnabled
    fun self(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun refactor(): Toggle
}
