package com.duckduckgo.adclick.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.adclick.impl.AdClickData
import com.duckduckgo.adclick.impl.DuckDuckGoAdClickData
import com.duckduckgo.adclick.impl.remoteconfig.AdClickAttributionFeature
import com.duckduckgo.adclick.impl.remoteconfig.AdClickAttributionRepository
import com.duckduckgo.adclick.impl.remoteconfig.RealAdClickAttributionRepository
import com.duckduckgo.adclick.impl.store.AdClickDatabase
import com.duckduckgo.adclick.impl.store.AdClickDatabase.Companion.ALL_MIGRATIONS
import com.duckduckgo.adclick.impl.store.exemptions.AdClickExemptionsDatabase
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
class AdClickModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAdClickDatabase(context: Context): AdClickDatabase {
        return Room.databaseBuilder(context, AdClickDatabase::class.java, "adclick.db")
            .addMigrations(*ALL_MIGRATIONS)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAdClickAttributionRepository(
        database: AdClickDatabase,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        @IsMainProcess isMainProcess: Boolean,
    ): AdClickAttributionRepository {
        return RealAdClickAttributionRepository(database, appCoroutineScope, dispatcherProvider, isMainProcess)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAdClickExemptionsDatabase(context: Context): AdClickExemptionsDatabase {
        return Room.databaseBuilder(context, AdClickExemptionsDatabase::class.java, "adclick_exemptions.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAdClickData(
        database: AdClickExemptionsDatabase,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        adClickAttributionFeature: AdClickAttributionFeature,
        @IsMainProcess isMainProcess: Boolean,
    ): AdClickData {
        return DuckDuckGoAdClickData(database, appCoroutineScope, dispatcherProvider, adClickAttributionFeature, isMainProcess)
    }
}
