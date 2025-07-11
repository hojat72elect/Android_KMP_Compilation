

package com.duckduckgo.app.global

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.browser.api.ActivityLifecycleCallbacks
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = ActivityLifecycleCallbacks::class,
)
private interface ActivityLifecycleCallbacksPluginPoint
