

package com.duckduckgo.app.notification

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.app.notification.model.SchedulableNotificationPlugin
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = SchedulableNotificationPlugin::class,
)
@Suppress("unused")
interface SchedulableNotificationPluginPoint
