

package com.duckduckgo.app.global.plugin

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@Suppress("unused")
interface PrimaryProcessLifecycleObserverPluginPoint
