package com.duckduckgo.contentscopescripts.impl

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.contentscopescripts.api.ContentScopeJsMessageHandlersPlugin
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = ContentScopeConfigPlugin::class,
)
@Suppress("unused")
interface ContentScopeConfigPluginPoint

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = ContentScopeJsMessageHandlersPlugin::class,
)
@Suppress("unused")
interface ContentScopeJsMessageHandlersPluginPoint
