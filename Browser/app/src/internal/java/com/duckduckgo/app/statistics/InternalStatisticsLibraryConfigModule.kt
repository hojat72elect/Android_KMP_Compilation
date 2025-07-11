package com.duckduckgo.app.statistics

import com.duckduckgo.app.di.StatisticsLibraryConfigModule
import com.duckduckgo.app.statistics.config.StatisticsLibraryConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(
    scope = AppScope::class,
    replaces = [StatisticsLibraryConfigModule::class],
)
class InternalStatisticsLibraryConfigModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideStatisticsLibraryConfig(): StatisticsLibraryConfig {
        return object : StatisticsLibraryConfig {
            override fun shouldFirePixelsAsDev(): Boolean {
                // for internal builds we always want to pixel as dev so that we can separate
                // internal users from real production users
                return true
            }
        }
    }
}
