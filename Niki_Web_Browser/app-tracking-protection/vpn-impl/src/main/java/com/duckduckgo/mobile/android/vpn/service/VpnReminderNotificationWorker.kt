

package com.duckduckgo.mobile.android.vpn.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.di.scopes.AppScope
import javax.inject.Inject

@ContributesWorker(AppScope::class)
class VpnReminderNotificationWorker(
    val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    @Inject
    lateinit var vpnReminderReceiverManager: VpnReminderReceiverManager

    override suspend fun doWork(): Result {
        vpnReminderReceiverManager.showReminderNotificationIfVpnDisabled(context)
        return Result.success()
    }

    companion object {
        const val WORKER_VPN_REMINDER_UNDESIRED_TAG = "VpnReminderNotificationUndesiredWorker"
        const val WORKER_VPN_REMINDER_DAILY_TAG = "VpnReminderNotificationDailyWorker"
    }
}
