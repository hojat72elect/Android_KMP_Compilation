

package com.duckduckgo.networkprotection.impl.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.ui.AppBreakageCategory
import com.duckduckgo.mobile.android.vpn.ui.OpenVpnBreakageCategoryWithBrokenApp
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.networkprotection.impl.R
import com.duckduckgo.networkprotection.impl.di.NetpBreakageCategories
import com.duckduckgo.subscriptions.api.PrivacyProFeedbackScreens.PrivacyProFeedbackScreenWithParams
import com.duckduckgo.subscriptions.api.PrivacyProUnifiedFeedback
import com.duckduckgo.subscriptions.api.PrivacyProUnifiedFeedback.PrivacyProFeedbackSource.VPN_MANAGEMENT
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.runBlocking

interface NetPNotificationActions {
    fun getReportIssueNotificationAction(context: Context): NotificationCompat.Action
    fun getEnableNetpNotificationAction(context: Context): NotificationCompat.Action
}

@ContributesBinding(AppScope::class)
class RealNetPNotificationActions @Inject constructor(
    private val globalActivityStarter: GlobalActivityStarter,
    @NetpBreakageCategories private val breakageCategories: Provider<List<AppBreakageCategory>>,
    private val privacyProUnifiedFeedback: PrivacyProUnifiedFeedback,
) : NetPNotificationActions {
    override fun getReportIssueNotificationAction(context: Context): NotificationCompat.Action {
        val launchIntent = if (runBlocking { privacyProUnifiedFeedback.shouldUseUnifiedFeedback(VPN_MANAGEMENT) }) {
            globalActivityStarter.startIntent(
                context,
                PrivacyProFeedbackScreenWithParams(feedbackSource = VPN_MANAGEMENT),
            )
        } else {
            globalActivityStarter.startIntent(
                context,
                OpenVpnBreakageCategoryWithBrokenApp(
                    launchFrom = "netp",
                    appName = "",
                    appPackageId = "",
                    breakageCategories = breakageCategories.get(),
                ),
            )
        }
        return NotificationCompat.Action(
            R.drawable.ic_baseline_feedback_24,
            context.getString(R.string.netpNotificationCTAReportIssue),
            PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE),
        )
    }

    override fun getEnableNetpNotificationAction(context: Context): NotificationCompat.Action {
        val launchIntent = Intent(context, NetPEnableReceiver::class.java).let { intent ->
            intent.action = NetPEnableReceiver.ACTION_NETP_ENABLE
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        return NotificationCompat.Action(
            R.drawable.ic_vpn_notification_24,
            context.getString(R.string.netpNotificationCTAEnableNetp),
            launchIntent,
        )
    }
}
