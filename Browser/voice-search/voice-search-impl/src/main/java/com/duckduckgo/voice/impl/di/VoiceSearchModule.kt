

package com.duckduckgo.voice.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.voice.api.VoiceSearchStatusListener
import com.duckduckgo.voice.impl.remoteconfig.RealVoiceSearchFeatureRepository
import com.duckduckgo.voice.impl.remoteconfig.VoiceSearchFeatureRepository
import com.duckduckgo.voice.store.ALL_MIGRATIONS
import com.duckduckgo.voice.store.RealVoiceSearchRepository
import com.duckduckgo.voice.store.SharedPreferencesVoiceSearchDataStore
import com.duckduckgo.voice.store.VoiceSearchDataStore
import com.duckduckgo.voice.store.VoiceSearchDatabase
import com.duckduckgo.voice.store.VoiceSearchRepository
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object VoiceSearchModule {
    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideVoiceSearchRepository(dataStore: VoiceSearchDataStore, voiceSearchStatusListener: VoiceSearchStatusListener): VoiceSearchRepository {
        return RealVoiceSearchRepository(dataStore, voiceSearchStatusListener)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideVoiceSearchDataStore(context: Context): VoiceSearchDataStore {
        return SharedPreferencesVoiceSearchDataStore(context)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideDatabase(context: Context): VoiceSearchDatabase {
        return Room.databaseBuilder(context, VoiceSearchDatabase::class.java, "voicesearch.db")
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideVoiceSearchFeatureRepository(
        database: VoiceSearchDatabase,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        @IsMainProcess isMainProcess: Boolean,
    ): VoiceSearchFeatureRepository {
        return RealVoiceSearchFeatureRepository(database, appCoroutineScope, dispatcherProvider, isMainProcess)
    }
}
