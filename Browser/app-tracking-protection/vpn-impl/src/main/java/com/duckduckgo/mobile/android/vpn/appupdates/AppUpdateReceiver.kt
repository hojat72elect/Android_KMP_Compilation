

package com.duckduckgo.mobile.android.vpn.appupdates

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import logcat.logcat

/**
 * We don't need to take any action here; but having this registered
 * ensures our VPN process is restarted after an app update
 */
class AppUpdateReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        logcat { "Notified of app update" }
    }
}
