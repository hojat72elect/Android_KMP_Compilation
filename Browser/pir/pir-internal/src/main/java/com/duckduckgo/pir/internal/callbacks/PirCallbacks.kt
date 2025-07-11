

package com.duckduckgo.pir.internal.callbacks

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import kotlinx.coroutines.CoroutineScope

interface PirCallbacks {
    fun onPirJobStarted(coroutineScope: CoroutineScope)
    fun onPirJobCompleted()
    fun onPirJobStopped()
}

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = PirCallbacks::class,
)
@Suppress("unused")
private interface PirCallbacksPluginPoint
