

package com.duckduckgo.networkprotection.internal.rekey

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.di.ProcessName
import com.duckduckgo.common.utils.extensions.registerNotExportedReceiver
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.service.VpnServiceCallbacks
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor
import com.duckduckgo.networkprotection.impl.rekey.RealNetPRekeyer
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat

@InjectWith(VpnScope::class)
@ContributesMultibinding(
    scope = VpnScope::class,
    boundType = VpnServiceCallbacks::class,
)
class DebugRekeyReceiver @Inject constructor(
    private val context: Context,
    private val rekeyer: RealNetPRekeyer,
    @ProcessName private val processName: String,
) : BroadcastReceiver(), VpnServiceCallbacks {

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        logcat { "onReceive ${intent.action} in $processName" }
        val pendingResult = goAsync()

        when (intent.action) {
            ACTION_FORCE_REKEY -> {
                logcat { "Force re-keying" }
                goAsync(pendingResult) {
                    rekeyer.forceRekey()
                }
            }

            else -> {
                logcat(LogPriority.WARN) { "Unknown action" }
                pendingResult?.finish()
            }
        }
    }

    override fun onVpnStarted(coroutineScope: CoroutineScope) {
        register()
    }

    override fun onVpnStopped(
        coroutineScope: CoroutineScope,
        vpnStopReason: VpnStateMonitor.VpnStopReason,
    ) {
        logcat { "Unregistering debug re-keying receiver" }
        unregister()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun register() {
        unregister()
        logcat { "Registering debug re-keying receiver" }
        context.registerNotExportedReceiver(
            this,
            IntentFilter().apply {
                addAction(ACTION_FORCE_REKEY)
            },
        )
    }

    private fun unregister() {
        kotlin.runCatching { context.unregisterReceiver(this) }
    }

    companion object {
        internal const val ACTION_FORCE_REKEY = "com.duckduckgo.networkprotection.internal.rekey.FORCE_REKEY"
    }
}

@Suppress("NoHardcodedCoroutineDispatcher")
private fun goAsync(
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
