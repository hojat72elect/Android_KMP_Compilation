

package com.duckduckgo.vpn.internal.feature.logs

import logcat.AndroidLogcatLogger
import logcat.LogcatLogger
import logcat.logcat

class TimberExtensions {
    companion object {
        @JvmStatic
        fun isLoggingEnabled(): Boolean {
            return LogcatLogger.isInstalled
        }

        @JvmStatic
        fun disableLogging() {
            logcat { "Logging Stopped" }
            LogcatLogger.uninstall()
        }

        @JvmStatic
        fun enableLogging() {
            LogcatLogger.install(AndroidLogcatLogger())
            logcat { "Logging Started" }
        }
    }
}
