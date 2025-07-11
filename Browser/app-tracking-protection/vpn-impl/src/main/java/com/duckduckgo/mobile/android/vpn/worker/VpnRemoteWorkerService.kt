

package com.duckduckgo.mobile.android.vpn.worker

import androidx.work.multiprocess.RemoteWorkerService
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.di.ProcessName
import com.duckduckgo.di.scopes.VpnScope
import dagger.android.AndroidInjection
import javax.inject.Inject
import logcat.AndroidLogcatLogger
import logcat.LogPriority.DEBUG
import logcat.LogcatLogger
import logcat.logcat

@InjectWith(VpnScope::class)
class VpnRemoteWorkerService constructor() : RemoteWorkerService() {
    @Inject
    @ProcessName
    lateinit var processName: String

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        LogcatLogger.install(AndroidLogcatLogger(DEBUG))
        logcat { "VPN-WORKER: running in process $processName" }
    }
}
