

package com.duckduckgo.mobile.android.vpn.ui.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.duckduckgo.mobile.android.vpn.R
import com.duckduckgo.mobile.android.vpn.apps.ui.ManageRecentAppsProtectionActivity
import com.duckduckgo.mobile.android.vpn.breakage.ReportBreakageAppListActivity

class NotificationActionReportIssue {

    companion object {
        fun reportIssueNotificationAction(context: Context): NotificationCompat.Action {
            val launchIntent = ReportBreakageAppListActivity.intent(context).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            return NotificationCompat.Action(
                R.drawable.ic_baseline_feedback_24,
                context.getString(R.string.atp_ReportIssue),
                PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE),
            )
        }

        fun mangeRecentAppsNotificationAction(context: Context): NotificationCompat.Action {
            val launchIntent = ManageRecentAppsProtectionActivity.intent(context).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            return NotificationCompat.Action(
                R.drawable.ic_baseline_feedback_24,
                context.getString(R.string.atp_ReportIssue),
                PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE),
            )
        }
    }
}
