package com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store.ALL_MIGRATIONS
import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store.NavigatorInterfaceDatabase
import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store.NavigatorInterfaceRepository
import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store.RealNavigatorInterfaceRepository
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object NavigatorInterfaceModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideNavigatorInterfaceDatabase(context: Context): NavigatorInterfaceDatabase {
        return Room.databaseBuilder(context, NavigatorInterfaceDatabase::class.java, "navigator_interface.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideNavigatorInterfaceRepository(
        database: NavigatorInterfaceDatabase,
        @AppCoroutineScope coroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
    ): NavigatorInterfaceRepository {
        return RealNavigatorInterfaceRepository(database, coroutineScope, dispatcherProvider)
    }
}
