

package com.duckduckgo.app.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.common.utils.plugins.worker.WorkerInjectorPlugin
import timber.log.Timber

class DaggerWorkerFactory(
    private val workerInjectorPluginPoint: PluginPoint<WorkerInjectorPlugin>,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        try {
            val workerClass = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
            val constructor = workerClass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
            val instance = constructor.newInstance(appContext, workerParameters)

            workerInjectorPluginPoint.getPlugins().forEach { plugin ->
                if (plugin.inject(instance)) {
                    Timber.i("Injected using plugin $workerClassName")
                    return@forEach
                }
                Timber.i("No injection required for worker $workerClassName")
            }

            return instance
        } catch (exception: Exception) {
            Timber.e(exception, "Worker $workerClassName could not be created")
            return null
        }
    }
}
