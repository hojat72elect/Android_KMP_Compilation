

package com.duckduckgo.history.impl

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.history.impl.store.ALL_MIGRATIONS
import com.duckduckgo.history.impl.store.HistoryDataStore
import com.duckduckgo.history.impl.store.HistoryDatabase
import com.duckduckgo.history.impl.store.SharedPreferencesHistoryDataStore
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
class HistoryModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideHistoryRepository(
        historyDatabase: HistoryDatabase,
        dispatcherProvider: DispatcherProvider,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        historyDataStore: HistoryDataStore,
    ): HistoryRepository {
        return RealHistoryRepository(historyDatabase.historyDao(), dispatcherProvider, appCoroutineScope, historyDataStore)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideDatabase(context: Context): HistoryDatabase {
        return Room.databaseBuilder(context, HistoryDatabase::class.java, "history.db")
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideHistoryDataStore(context: Context): HistoryDataStore {
        return SharedPreferencesHistoryDataStore(context)
    }
}
