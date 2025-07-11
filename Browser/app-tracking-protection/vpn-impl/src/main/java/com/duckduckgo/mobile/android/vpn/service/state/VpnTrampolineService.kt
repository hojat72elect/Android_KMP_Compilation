

package com.duckduckgo.mobile.android.vpn.service.state

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.duckduckgo.mobile.android.vpn.service.TrackerBlockingVpnService
import logcat.logcat

/**
 * This is a trampoline service that is used to start the VPN service.
 * The reason why we go through this is because we want to make sure the :vpn process is already created when we call
 * startForegroundService() method. That should reduce the time between startForegroundService() and startForeground()
 */
internal class VpnTrampolineService : Service() {

    override fun onCreate() {
        logcat { "onCreate" }
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logcat { "onStartCommand" }
        return super.onStartCommand(intent, flags, startId).also {
            TrackerBlockingVpnService.startVpnService(this)
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        logcat { "Bound to VPN" }
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        logcat { "Unbound from VPN" }
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        logcat { "onDestroy" }
        super.onDestroy()
    }
}
