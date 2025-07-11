

package com.duckduckgo.mobile.android.vpn.service

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.VpnScope

@ContributesPluginPoint(
    scope = VpnScope::class,
    boundType = VpnEnabledNotificationContentPlugin::class,
)
@Suppress("unused")
interface VpnEnabledNotificationContentPluginPoint

fun PluginPoint<VpnEnabledNotificationContentPlugin>.getHighestPriorityPlugin(): VpnEnabledNotificationContentPlugin? {
    return runCatching { getPlugins().filter { it.isActive() }.maxByOrNull { it.getPriority().ordinal } }.getOrNull()
}
