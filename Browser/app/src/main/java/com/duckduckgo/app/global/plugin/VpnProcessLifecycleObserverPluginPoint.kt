

package com.duckduckgo.app.global.plugin

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.app.lifecycle.VpnProcessLifecycleObserver
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = VpnProcessLifecycleObserver::class,
)
@Suppress("unused")
interface VpnProcessLifecycleObserverPluginPoint
