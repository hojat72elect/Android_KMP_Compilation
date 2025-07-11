

package com.duckduckgo.mobile.android.vpn.service.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.duckduckgo.mobile.android.vpn.service.VpnReminderReceiver
import com.duckduckgo.mobile.android.vpn.service.VpnReminderReceiver.Companion.ACTION_VPN_REMINDER_RESTART
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.DeviceShieldTrackerActivity

internal fun getAppTPNotificationPressIntent(context: Context): PendingIntent? {
    return TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(DeviceShieldTrackerActivity.intent(context = context))
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}

internal fun getAppTPStartIntent(context: Context): PendingIntent? {
    return Intent(context, VpnReminderReceiver::class.java).let { intent ->
        intent.action = ACTION_VPN_REMINDER_RESTART
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}
