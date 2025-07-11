

package com.duckduckgo.app.usage.di

import com.duckduckgo.app.usage.app.AppDaysUsedDao
import com.duckduckgo.app.usage.app.AppDaysUsedDatabaseRepository
import com.duckduckgo.app.usage.app.AppDaysUsedRepository
import com.duckduckgo.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
class AppUsageModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun appDaysUsedRepository(appDaysUsedDao: AppDaysUsedDao): AppDaysUsedRepository {
        return AppDaysUsedDatabaseRepository(appDaysUsedDao)
    }
}
