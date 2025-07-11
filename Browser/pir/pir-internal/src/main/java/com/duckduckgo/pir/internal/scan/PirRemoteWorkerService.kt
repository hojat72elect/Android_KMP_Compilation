

package com.duckduckgo.pir.internal.scan

import android.os.Process
import androidx.work.multiprocess.RemoteWorkerService
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import logcat.LogcatLogger
import logcat.logcat

class PirRemoteWorkerService : RemoteWorkerService() {
    override fun onCreate() {
        super.onCreate()
        LogcatLogger.install(AndroidLogcatLogger(LogPriority.DEBUG))
        logcat { "PIR-WORKER: Running in process: ${Process.myPid()}" }
    }
}
