

package com.duckduckgo.request.filterer.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.request.filterer.store.ALL_MIGRATIONS
import com.duckduckgo.request.filterer.store.RealRequestFiltererFeatureToggleRepository
import com.duckduckgo.request.filterer.store.RealRequestFiltererFeatureToggleStore
import com.duckduckgo.request.filterer.store.RealRequestFiltererRepository
import com.duckduckgo.request.filterer.store.RequestFiltererDatabase
import com.duckduckgo.request.filterer.store.RequestFiltererFeatureToggleRepository
import com.duckduckgo.request.filterer.store.RequestFiltererFeatureToggleStore
import com.duckduckgo.request.filterer.store.RequestFiltererRepository
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object RequestFiltererModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideRequestFiltererDatabase(context: Context): RequestFiltererDatabase {
        return Room.databaseBuilder(context, RequestFiltererDatabase::class.java, "request_filterer.db")
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideRequestFiltererRepository(
        database: RequestFiltererDatabase,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        @IsMainProcess isMainProcess: Boolean,
    ): RequestFiltererRepository {
        return RealRequestFiltererRepository(database, appCoroutineScope, dispatcherProvider, isMainProcess)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideRequestFiltererFeatureToggleRepository(
        requestFilterFeatureToggleStore: RequestFiltererFeatureToggleStore,
    ): RequestFiltererFeatureToggleRepository {
        return RealRequestFiltererFeatureToggleRepository(requestFilterFeatureToggleStore)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideRequestFiltererFeatureToggleStore(context: Context): RequestFiltererFeatureToggleStore {
        return RealRequestFiltererFeatureToggleStore(context)
    }
}
