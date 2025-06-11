

package com.duckduckgo.webcompat.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.webcompat.store.ALL_MIGRATIONS
import com.duckduckgo.webcompat.store.RealWebCompatRepository
import com.duckduckgo.webcompat.store.WebCompatDatabase
import com.duckduckgo.webcompat.store.WebCompatRepository
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object WebCompatModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideWebCompatDatabase(context: Context): WebCompatDatabase {
        return Room.databaseBuilder(context, WebCompatDatabase::class.java, "web_compat.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideWebCompatRepository(
        database: WebCompatDatabase,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        @IsMainProcess isMainProcess: Boolean,
    ): WebCompatRepository {
        return RealWebCompatRepository(database, appCoroutineScope, dispatcherProvider, isMainProcess)
    }
}
