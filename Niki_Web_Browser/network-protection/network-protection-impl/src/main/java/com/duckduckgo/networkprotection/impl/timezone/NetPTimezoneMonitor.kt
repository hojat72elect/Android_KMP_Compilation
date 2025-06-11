

package com.duckduckgo.networkprotection.impl.timezone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.duckduckgo.common.utils.extensions.registerNotExportedReceiver
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.service.VpnServiceCallbacks
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor
import com.duckduckgo.networkprotection.impl.NetPVpnFeature
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import logcat.logcat

@SingleInstanceIn(VpnScope::class)
@ContributesMultibinding(
    scope = VpnScope::class,
    boundType = VpnServiceCallbacks::class,
)
class NetPTimezoneMonitor @Inject constructor(
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
    private val context: Context,
) : BroadcastReceiver(), VpnServiceCallbacks {
    override fun onVpnStarted(coroutineScope: CoroutineScope) {
        if (runBlocking { !vpnFeaturesRegistry.isFeatureRegistered(NetPVpnFeature.NETP_VPN) }) {
            logcat { "NetP not enabled, skip registering timezone monitor" }
            return
        }

        logcat { "Registering NetP TimeZone monitor" }
        register()
    }

    override fun onVpnStopped(coroutineScope: CoroutineScope, vpnStopReason: VpnStateMonitor.VpnStopReason) {
        logcat { "Unregistering NetP TimeZone monitor" }
        unregister()
    }

    override fun onReceive(context: Context?, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_TIMEZONE_CHANGED -> {
                val pendingResult = goAsync()
                execAsync(pendingResult) {
                    vpnFeaturesRegistry.refreshFeature(NetPVpnFeature.NETP_VPN)
                }
            }
        }
    }

    private fun register() {
        runCatching { context.unregisterReceiver(this) }

        IntentFilter().apply {
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }.run {
            context.registerNotExportedReceiver(this@NetPTimezoneMonitor, this)
        }
    }

    private fun unregister() {
        runCatching { context.unregisterReceiver(this) }
    }
}

@Suppress("NoHardcodedCoroutineDispatcher")
private fun execAsync(
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
