

package com.duckduckgo.app.di

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.common.utils.plugins.worker.WorkerInjectorPlugin
import com.duckduckgo.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
object WorkerModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun workManager(
        context: Context,
        workerFactory: WorkerFactory,
    ): WorkManager {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(context, config)
        return WorkManager.getInstance(context)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun workerFactory(
        workerInjectorPluginPoint: PluginPoint<WorkerInjectorPlugin>,
    ): WorkerFactory {
        return DaggerWorkerFactory(workerInjectorPluginPoint)
    }
}
