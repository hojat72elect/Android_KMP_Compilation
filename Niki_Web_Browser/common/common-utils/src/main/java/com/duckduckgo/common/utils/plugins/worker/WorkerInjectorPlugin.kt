

package com.duckduckgo.common.utils.plugins.worker

import androidx.work.ListenableWorker
import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(AppScope::class)
interface WorkerInjectorPlugin {
    /**
     * @return whether the worker has been injected
     */
    fun inject(worker: ListenableWorker): Boolean
}
