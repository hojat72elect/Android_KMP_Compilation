

package com.duckduckgo.app.global

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process

abstract class MultiProcessApplication : Application() {
    private val shortProcessNameCached: String by lazy { shortProcessName }
    private val isMainProcessCached: Boolean by lazy { isMainProcess }

    final override fun onCreate() {
        super.onCreate()
        if (isMainProcessCached) {
            onMainProcessCreate()
        } else {
            onSecondaryProcessCreate(shortProcessNameCached)
        }
    }

    abstract fun onMainProcessCreate()

    open fun onSecondaryProcessCreate(shortProcessName: String) {}
}

inline val Application.shortProcessName: String
    get() = currentProcessName?.substringAfter(delimiter = "$packageName:", missingDelimiterValue = "UNKNOWN") ?: "UNKNOWN"

inline val Application.isMainProcess: Boolean
    get() = packageName == currentProcessName

inline fun Context.runInSecondaryProcessNamed(
    name: String,
    block: () -> Unit,
) {
    if (currentProcessName == "$packageName:$name") {
        block()
    }
}

val Context.currentProcessName: String?
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        Application.getProcessName()
    } else {
        processNameFromSystemService()
    }

private fun Context.processNameFromSystemService(): String {
    val am = this.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager?
    return am?.runningAppProcesses?.firstOrNull { it.pid == Process.myPid() }?.processName.orEmpty()
}
