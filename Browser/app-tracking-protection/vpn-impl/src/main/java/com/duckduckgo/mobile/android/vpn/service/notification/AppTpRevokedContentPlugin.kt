

package com.duckduckgo.mobile.android.vpn.service.notification

import android.content.Context
import androidx.core.app.NotificationCompat.Action
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.R
import com.duckduckgo.mobile.android.vpn.R.drawable
import com.duckduckgo.mobile.android.vpn.R.string
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin.NotificationContent
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin.NotificationPriority
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin.NotificationPriority.NORMAL
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin.Type
import com.duckduckgo.mobile.android.vpn.service.VpnReminderNotificationContentPlugin.Type.REVOKED
import com.duckduckgo.mobile.android.vpn.ui.notification.NotificationActionReportIssue
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class AppTpRevokedContentPlugin @Inject constructor(
    private val context: Context,
) : VpnReminderNotificationContentPlugin {
    private val notificationPendingIntent by lazy { getAppTPNotificationPressIntent(context) }
    private val notificationActionPendingIntent by lazy { getAppTPStartIntent(context) }

    override fun getContent(): NotificationContent? {
        return NotificationContent(
            isSilent = true,
            shouldAutoCancel = null,
            title = context.getString(R.string.atp_RevokedNotification),
            onNotificationPressIntent = notificationPendingIntent,
            notificationAction = listOf(
                Action(
                    drawable.ic_vpn_notification_24,
                    context.getString(string.atp_EnableCTA),
                    notificationActionPendingIntent,
                ),
                NotificationActionReportIssue.reportIssueNotificationAction(context),
            ),
        )
    }

    override fun getPriority(): NotificationPriority {
        return NORMAL
    }

    override fun getType(): Type = REVOKED
}
