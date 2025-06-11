

package com.duckduckgo.app.di

import com.duckduckgo.app.statistics.config.DefaultStatisticsLibraryConfig
import com.duckduckgo.app.statistics.config.StatisticsLibraryConfig
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object StatisticsLibraryConfigModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideStatisticsLibraryConfig(
        appBuildConfig: AppBuildConfig,
    ): StatisticsLibraryConfig = DefaultStatisticsLibraryConfig(appBuildConfig)
}
