

package com.duckduckgo.mobile.android.vpn.service.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.di.scopes.ReceiverScope
import dagger.android.AndroidInjection
import javax.inject.Inject
import logcat.LogPriority
import logcat.logcat

@InjectWith(ReceiverScope::class)
class PersistentNotificationDismissedReceiver : BroadcastReceiver() {
    @Inject
    lateinit var vpnNotificationStore: VpnNotificationStore

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        AndroidInjection.inject(this, context)

        logcat { "PersistentNotificationDismissedReceiver onReceive ${intent.action}" }
        val pendingResult = goAsync()

        if (intent.action == ACTION_VPN_PERSISTENT_NOTIF_DISMISSED) {
            // handle dismissed notif
            com.duckduckgo.mobile.android.vpn.service.goAsync(pendingResult) {
                logcat { "PersistentNotificationDismissedReceiver dismissed notification received" }
                vpnNotificationStore.persistentNotifDimissedTimestamp = System.currentTimeMillis()
            }
        } else {
            logcat(LogPriority.WARN) { "PersistentNotificationDismissedReceiver: unknown action ${intent.action}" }
            pendingResult?.finish()
        }
    }

    companion object {
        internal const val ACTION_VPN_PERSISTENT_NOTIF_DISMISSED = "com.duckduckgo.vpn.ACTION_VPN_PERSISTENT_NOTIF_DISMISSED"
    }
}
