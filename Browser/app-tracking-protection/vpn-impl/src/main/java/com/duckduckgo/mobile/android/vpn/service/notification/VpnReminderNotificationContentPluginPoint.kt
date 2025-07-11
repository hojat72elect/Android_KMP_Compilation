

package com.duckduckgo.mobile.android.vpn.service.notification

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = VpnReminderNotificationContentPlugin::class,
)
@Suppress("unused")
interface VpnReminderNotificationContentPluginPoint

fun PluginPoint<VpnReminderNotificationContentPlugin>.getHighestPriorityPluginForType(
    type: VpnReminderNotificationContentPlugin.Type,
): VpnReminderNotificationContentPlugin? {
    return runCatching {
        getPlugins().filter { it.getType() == type }.maxByOrNull { it.getPriority().ordinal }
    }.getOrNull()
}
