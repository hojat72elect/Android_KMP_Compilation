

package com.duckduckgo.sync.impl.engine

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(AppScope::class)
interface SyncEngineLifecycle {
    fun onSyncEnabled()
    fun onSyncDisabled()
}
