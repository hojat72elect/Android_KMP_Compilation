

package com.duckduckgo.common.ui.tabs

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.feature.toggles.api.Toggle.InternalAlwaysEnabled

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "swipingTabs",
)
interface SwipingTabsFeature {
    // The main kill switch for the feature
    @Toggle.DefaultValue(DefaultFeatureValue.INTERNAL)
    fun self(): Toggle

    // The toggle used for staged rollout to external users
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @InternalAlwaysEnabled
    fun enabledForUsers(): Toggle
}
