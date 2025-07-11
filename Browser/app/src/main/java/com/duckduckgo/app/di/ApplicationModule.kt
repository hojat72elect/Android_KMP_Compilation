

package com.duckduckgo.app.di

import android.app.Application
import android.content.Context
import com.duckduckgo.app.global.currentProcessName
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
abstract class ApplicationModule {

    @SingleInstanceIn(AppScope::class)
    @Binds
    abstract fun bindContext(application: Application): Context
}

@Module
@ContributesTo(AppScope::class)
object ProcessNameModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    @ProcessName
    fun provideProcessName(context: Context): String {
        val process = runCatching {
            context.currentProcessName?.substringAfter(delimiter = context.packageName, missingDelimiterValue = "UNKNOWN") ?: "UNKNOWN"
        }.getOrDefault("ERROR")

        // When is the main process 'currentProcessName' returns the package name and so `process` is empty string
        return process.ifEmpty {
            "main"
        }
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    @IsMainProcess
    fun providerIsMainProcess(@ProcessName processName: String): Boolean {
        return processName == "main"
    }
}
