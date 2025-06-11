

package com.duckduckgo.remote.messaging.impl.mappers

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.MessageActionMapperPlugin

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = MessageActionMapperPlugin::class,
)
@Suppress("unused")
interface UnusedRemoteMessageNavigationActionPluginPoint
