

@file:Suppress("unused")

package com.duckduckgo.app.global.performance

import android.util.Log
import timber.log.Timber

object PerformanceConstants {
    const val NANO_TO_MILLIS_DIVISOR = 1_000_000.0
}

inline fun <T> measureExecution(
    logMessage: String,
    logLevel: Int = Log.DEBUG,
    function: () -> T,
): T {
    val startTime = System.nanoTime()
    return function.invoke().also {
        val difference = (System.nanoTime() - startTime) / PerformanceConstants.NANO_TO_MILLIS_DIVISOR
        Timber.log(logLevel, "$logMessage; took ${difference}ms")
    }
}
