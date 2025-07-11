

package com.duckduckgo.app.browser.rating.di

import com.duckduckgo.app.browser.rating.db.AppEnjoymentDao
import com.duckduckgo.app.browser.rating.db.AppEnjoymentDatabaseRepository
import com.duckduckgo.app.browser.rating.db.AppEnjoymentRepository
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.app.global.rating.*
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.usage.app.AppDaysUsedRepository
import com.duckduckgo.app.usage.search.SearchCountDao
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.playstore.PlayStoreUtils
import com.duckduckgo.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import dagger.multibindings.IntoSet
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Module
class RatingModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    @IntoSet
    fun appEnjoymentManagerObserver(
        appEnjoymentPromptEmitter: AppEnjoymentPromptEmitter,
        promptTypeDecider: PromptTypeDecider,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
    ): MainProcessLifecycleObserver {
        return AppEnjoymentAppCreationObserver(appEnjoymentPromptEmitter, promptTypeDecider, appCoroutineScope)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun appEnjoymentPromptEmitter(): AppEnjoymentPromptEmitter {
        return AppEnjoymentLiveDataEmitter()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun appEnjoymentUserEventRecorder(
        appEnjoymentRepository: AppEnjoymentRepository,
        appEnjoymentPromptEmitter: AppEnjoymentPromptEmitter,
    ): AppEnjoymentUserEventRecorder {
        return AppEnjoymentUserEventDatabaseRecorder(appEnjoymentRepository, appEnjoymentPromptEmitter)
    }

    @Provides
    fun promptTypeDecider(
        playStoreUtils: PlayStoreUtils,
        searchCountDao: SearchCountDao,
        @Named(INITIAL_PROMPT_DECIDER_NAME) initialPromptDecider: ShowPromptDecider,
        @Named(SECONDARY_PROMPT_DECIDER_NAME) secondaryPromptDecider: ShowPromptDecider,
        appBuildConfig: AppBuildConfig,
        dispatchers: DispatcherProvider,
    ): PromptTypeDecider {
        return InitialPromptTypeDecider(
            playStoreUtils,
            searchCountDao,
            initialPromptDecider,
            secondaryPromptDecider,
            dispatchers,
            appBuildConfig,
        )
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun appEnjoymentDao(database: AppDatabase): AppEnjoymentDao {
        return database.appEnjoymentDao()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun appEnjoymentRepository(appEnjoymentDao: AppEnjoymentDao): AppEnjoymentRepository {
        return AppEnjoymentDatabaseRepository(appEnjoymentDao)
    }

    @Named(INITIAL_PROMPT_DECIDER_NAME)
    @Provides
    fun initialPromptDecider(
        appDaysUsedRepository: AppDaysUsedRepository,
        appEnjoymentRepository: AppEnjoymentRepository,
    ): ShowPromptDecider {
        return InitialPromptDecider(appDaysUsedRepository, appEnjoymentRepository)
    }

    @Named(SECONDARY_PROMPT_DECIDER_NAME)
    @Provides
    fun secondaryPromptDecider(
        appDaysUsedRepository: AppDaysUsedRepository,
        appEnjoymentRepository: AppEnjoymentRepository,
    ): ShowPromptDecider {
        return SecondaryPromptDecider(appDaysUsedRepository, appEnjoymentRepository)
    }

    companion object {
        private const val INITIAL_PROMPT_DECIDER_NAME = "initial-prompt-decider"
        private const val SECONDARY_PROMPT_DECIDER_NAME = "secondary-prompt-decider"
    }
}
