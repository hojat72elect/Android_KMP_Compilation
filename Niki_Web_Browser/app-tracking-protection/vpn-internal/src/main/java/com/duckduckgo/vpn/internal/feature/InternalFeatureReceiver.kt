

package com.duckduckgo.vpn.internal.feature

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.duckduckgo.common.utils.extensions.registerExportedReceiver

/**
 * Abstract class to create generic receivers for internal features accessible through
 * adb commands.
 *
 * Implement the [intentAction] function and return the intent action this receiver will be
 * listening to
 */
abstract class InternalFeatureReceiver(
    private val context: Context,
    private val receiver: (Intent) -> Unit,
) : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        receiver(intent)
    }

    abstract fun intentAction(): String

    fun register() {
        unregister()
        // needs exported because it's registered    on the :vpn process but can be intent-trigger from the :app process
        context.registerExportedReceiver(this, IntentFilter(intentAction()))
    }

    fun unregister() {
        kotlin.runCatching { context.unregisterReceiver(this) }
    }
}
