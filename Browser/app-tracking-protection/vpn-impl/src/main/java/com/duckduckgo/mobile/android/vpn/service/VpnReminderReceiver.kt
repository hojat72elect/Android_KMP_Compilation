

package com.duckduckgo.mobile.android.vpn.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.di.scopes.ReceiverScope
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.pixels.DeviceShieldPixels
import dagger.android.AndroidInjection
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat

@InjectWith(ReceiverScope::class)
class VpnReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var deviceShieldPixels: DeviceShieldPixels

    @Inject lateinit var vpnFeaturesRegistry: VpnFeaturesRegistry

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        AndroidInjection.inject(this, context)

        logcat { "VpnReminderReceiver onReceive ${intent.action}" }
        val pendingResult = goAsync()

        if (intent.action == ACTION_VPN_REMINDER_RESTART) {
            logcat { "Vpn will restart because the user asked it" }
            deviceShieldPixels.enableFromReminderNotification()
            goAsync(pendingResult) {
                vpnFeaturesRegistry.registerFeature(AppTpVpnFeature.APPTP_VPN)
            }
        } else {
            logcat(LogPriority.WARN) { "VpnReminderReceiver: unknown action" }
            pendingResult?.finish()
        }
    }

    companion object {
        internal const val ACTION_VPN_REMINDER_RESTART = "com.duckduckgo.vpn.internaltesters.reminder.restart"
    }
}

@Suppress("NoHardcodedCoroutineDispatcher")
fun goAsync(
    pendingResult: BroadcastReceiver.PendingResult?,
    coroutineScope: CoroutineScope = GlobalScope,
    block: suspend () -> Unit,
) {
    coroutineScope.launch(Dispatchers.IO) {
        try {
            block()
        } finally {
            // Always call finish(), even if the coroutineScope was cancelled
            pendingResult?.finish()
        }
    }
}
