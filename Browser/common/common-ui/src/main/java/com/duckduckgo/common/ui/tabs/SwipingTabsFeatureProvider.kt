

package com.duckduckgo.common.ui.tabs

import com.duckduckgo.di.scopes.AppScope
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(AppScope::class)
class SwipingTabsFeatureProvider @Inject constructor(
    swipingTabsFeature: SwipingTabsFeature,
) {
    val isEnabled: Boolean by lazy {
        swipingTabsFeature.self().isEnabled() && swipingTabsFeature.enabledForUsers().isEnabled()
    }
}
