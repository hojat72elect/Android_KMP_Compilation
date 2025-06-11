

package com.duckduckgo.app.notification

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.app.notification.model.NotificationPlugin
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = NotificationPlugin::class,
)
@Suppress("unused")
interface NotificationPluginPoint
