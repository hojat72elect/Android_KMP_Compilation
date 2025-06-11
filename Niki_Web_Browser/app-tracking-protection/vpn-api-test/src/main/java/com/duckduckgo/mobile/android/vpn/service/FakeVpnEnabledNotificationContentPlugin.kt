

package com.duckduckgo.mobile.android.vpn.service

import com.duckduckgo.mobile.android.vpn.service.VpnEnabledNotificationContentPlugin.VpnEnabledNotificationPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeVpnEnabledNotificationContentPlugin constructor(
    private val isActive: Boolean,
    private val priority: VpnEnabledNotificationPriority = VpnEnabledNotificationPriority.NORMAL,
) : VpnEnabledNotificationContentPlugin {

    override val uuid: String = "1234"

    override fun getInitialContent(): VpnEnabledNotificationContentPlugin.VpnEnabledNotificationContent? {
        return null
    }

    override fun getUpdatedContent(): Flow<VpnEnabledNotificationContentPlugin.VpnEnabledNotificationContent?> {
        return flowOf(null)
    }

    override fun getPriority(): VpnEnabledNotificationPriority {
        return this.priority
    }

    override fun isActive(): Boolean = this.isActive
}
