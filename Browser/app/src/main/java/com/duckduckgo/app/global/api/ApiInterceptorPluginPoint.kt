

package com.duckduckgo.app.global.api

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
@Suppress("unused")
interface ApiInterceptorPluginPoint
