

package com.duckduckgo.sync.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.favicons.FaviconsFetchingPrompt
import com.duckduckgo.sync.api.favicons.FaviconsFetchingStore
import com.duckduckgo.sync.crypto.SyncLib
import com.duckduckgo.sync.impl.AppQREncoder
import com.duckduckgo.sync.impl.QREncoder
import com.duckduckgo.sync.impl.SyncAccountRepository
import com.duckduckgo.sync.impl.engine.AppSyncStateRepository
import com.duckduckgo.sync.impl.engine.SyncStateRepository
import com.duckduckgo.sync.impl.error.RealSyncApiErrorRepository
import com.duckduckgo.sync.impl.error.RealSyncOperationErrorRepository
import com.duckduckgo.sync.impl.error.SyncApiErrorRepository
import com.duckduckgo.sync.impl.error.SyncOperationErrorRepository
import com.duckduckgo.sync.impl.favicons.SyncFaviconFetchingStore
import com.duckduckgo.sync.impl.favicons.SyncFaviconsFetchingPrompt
import com.duckduckgo.sync.impl.internal.AppSyncInternalEnvDataStore
import com.duckduckgo.sync.impl.internal.SyncInternalEnvDataStore
import com.duckduckgo.sync.impl.stats.RealSyncStatsRepository
import com.duckduckgo.sync.impl.stats.SyncStatsRepository
import com.duckduckgo.sync.store.SharedPrefsProvider
import com.duckduckgo.sync.store.SyncDatabase
import com.duckduckgo.sync.store.SyncSharedPrefsProvider
import com.duckduckgo.sync.store.SyncSharedPrefsStore
import com.duckduckgo.sync.store.SyncStore
import com.duckduckgo.sync.store.SyncUnavailableSharedPrefsStore
import com.duckduckgo.sync.store.SyncUnavailableStore
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import javax.inject.Qualifier
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object SyncStoreModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesSyncStore(
        sharedPrefsProvider: SharedPrefsProvider,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
    ): SyncStore {
        return SyncSharedPrefsStore(sharedPrefsProvider, appCoroutineScope, dispatcherProvider)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesSyncInternalEnvStore(
        context: Context,
    ): SyncInternalEnvDataStore {
        return AppSyncInternalEnvDataStore(context)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideSharedPrefsProvider(context: Context): SharedPrefsProvider {
        return SyncSharedPrefsProvider(context)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesNativeLib(context: Context): SyncLib {
        return SyncLib.create(context)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideQREncoder(
        context: Context,
    ): QREncoder {
        // create instance of BarcodeEncoder here so we don't need to add third-party dependency to :app module (classpath)
        return AppQREncoder(context, BarcodeEncoder())
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideSyncDatabase(context: Context): SyncDatabase {
        return Room.databaseBuilder(context, SyncDatabase::class.java, "sync.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideSyncStateRepository(syncDatabase: SyncDatabase): SyncStateRepository {
        return AppSyncStateRepository(syncDatabase.syncAttemptsDao())
    }

    @Provides
    fun provideSyncApiErrorRepository(syncDatabase: SyncDatabase): SyncApiErrorRepository {
        return RealSyncApiErrorRepository(syncDatabase.syncApiErrorsDao())
    }

    @Provides
    fun provideSyncOperationErrorRepository(syncDatabase: SyncDatabase): SyncOperationErrorRepository {
        return RealSyncOperationErrorRepository(syncDatabase.syncOperationErrorsDao())
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideSyncStatsRepository(
        syncStateRepository: SyncStateRepository,
        syncApiErrorRepository: SyncApiErrorRepository,
        syncOperationErrorRepository: SyncOperationErrorRepository,
    ): SyncStatsRepository {
        return RealSyncStatsRepository(syncStateRepository, syncApiErrorRepository, syncOperationErrorRepository)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideSyncFaviconsFetchingStore(
        context: Context,
    ): FaviconsFetchingStore {
        return SyncFaviconFetchingStore(context)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideSyncFaviconsFetchingPrompt(
        faviconFetchingStore: FaviconsFetchingStore,
        syncAccountRepository: SyncAccountRepository,
    ): FaviconsFetchingPrompt {
        return SyncFaviconsFetchingPrompt(faviconFetchingStore, syncAccountRepository)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideSyncPausedStore(
        sharedPrefsProvider: SharedPrefsProvider,
    ): SyncUnavailableStore {
        return SyncUnavailableSharedPrefsStore(sharedPrefsProvider)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "com.duckduckgo.sync.sync_promotion",
    )

    @Provides
    @SingleInstanceIn(AppScope::class)
    @SyncPromotion
    fun provideSyncPromotionsDataStore(context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

@Qualifier
annotation class SyncPromotion
