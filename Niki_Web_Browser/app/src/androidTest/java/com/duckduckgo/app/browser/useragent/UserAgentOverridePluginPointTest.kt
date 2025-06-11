package com.duckduckgo.app.browser.useragent

import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.user.agent.api.UserAgentInterceptor

fun provideUserAgentOverridePluginPoint(): PluginPoint<UserAgentInterceptor> {
    return object : PluginPoint<UserAgentInterceptor> {
        override fun getPlugins(): Collection<UserAgentInterceptor> {
            return listOf()
        }
    }
}
