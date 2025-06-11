

package com.duckduckgo.mobile.android.vpn.service

import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin.NotificationContent
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin.NotificationPriority
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin.Type

class FakeVpnReminderNotificationContentPlugin constructor(
    private val type: Type,
    private val priority: NotificationPriority = NotificationPriority.NORMAL,
) : VpnReminderNotificationContentPlugin {

    override fun getContent(): NotificationContent? = null

    override fun getType(): Type = this.type

    override fun getPriority(): NotificationPriority {
        return this.priority
    }
}
