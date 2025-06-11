

package com.duckduckgo.user.agent.impl

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.user.agent.api.UserAgentInterceptor

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = UserAgentInterceptor::class,
)
@Suppress("unused")
interface UserAgentInterceptorPluginPoint
