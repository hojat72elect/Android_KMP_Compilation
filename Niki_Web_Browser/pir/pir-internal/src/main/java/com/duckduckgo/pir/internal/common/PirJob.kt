

package com.duckduckgo.pir.internal.common

import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.pir.internal.callbacks.PirCallbacks
import kotlinx.coroutines.CoroutineScope
import logcat.logcat

abstract class PirJob(private val callbacks: PluginPoint<PirCallbacks>) {
    fun onJobStarted(coroutineScope: CoroutineScope) {
        callbacks.getPlugins().forEach {
            logcat { "PIR-CALLBACKS: Starting $it" }
            it.onPirJobStarted(coroutineScope)
        }
    }

    fun onJobCompleted() {
        callbacks.getPlugins().forEach {
            logcat { "PIR-CALLBACKS: Completing $it" }
            it.onPirJobCompleted()
        }
    }

    fun onJobStopped() {
        callbacks.getPlugins().forEach {
            logcat { "PIR-CALLBACKS: Stopping $it" }
            it.onPirJobStopped()
        }
    }
}
