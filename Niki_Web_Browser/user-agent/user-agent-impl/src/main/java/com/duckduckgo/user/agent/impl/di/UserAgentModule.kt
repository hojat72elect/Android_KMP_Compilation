

package com.duckduckgo.user.agent.impl.di

import android.content.Context
import android.webkit.WebSettings
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.user.agent.impl.RealUserAgentProvider
import com.duckduckgo.user.agent.store.ALL_MIGRATIONS
import com.duckduckgo.user.agent.store.RealUserAgentFeatureToggleRepository
import com.duckduckgo.user.agent.store.RealUserAgentFeatureToggleStore
import com.duckduckgo.user.agent.store.RealUserAgentRepository
import com.duckduckgo.user.agent.store.UserAgentDatabase
import com.duckduckgo.user.agent.store.UserAgentFeatureToggleRepository
import com.duckduckgo.user.agent.store.UserAgentFeatureToggleStore
import com.duckduckgo.user.agent.store.UserAgentRepository
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@ContributesTo(AppScope::class)
@Module
class UserAgentModule {
    @SingleInstanceIn(AppScope::class)
    @Provides
    @Named("defaultUserAgent")
    fun provideDefaultUserAgent(context: Context): String {
        return runCatching {
            WebSettings.getDefaultUserAgent(context)
        }.getOrDefault(RealUserAgentProvider.fallbackDefaultUA)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideUserAgentRepository(
        database: UserAgentDatabase,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        @IsMainProcess isMainProcess: Boolean,
    ): UserAgentRepository {
        return RealUserAgentRepository(
            database = database,
            coroutineScope = appCoroutineScope,
            dispatcherProvider = dispatcherProvider,
            isMainProcess = isMainProcess,
        )
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideUserAgentFeatureToggleStore(context: Context): UserAgentFeatureToggleStore {
        return RealUserAgentFeatureToggleStore(context)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideUserAgentFeatureToggleRepository(
        userAgentFeatureToggleStore: UserAgentFeatureToggleStore,
    ): UserAgentFeatureToggleRepository {
        return RealUserAgentFeatureToggleRepository(
            userAgentFeatureToggleStore = userAgentFeatureToggleStore,
        )
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideDatabase(context: Context): UserAgentDatabase {
        return Room.databaseBuilder(context, UserAgentDatabase::class.java, "user_agent.db")
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }
}
