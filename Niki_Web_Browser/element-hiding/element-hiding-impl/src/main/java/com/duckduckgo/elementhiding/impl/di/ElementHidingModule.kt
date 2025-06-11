

package com.duckduckgo.elementhiding.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.elementhiding.store.ALL_MIGRATIONS
import com.duckduckgo.elementhiding.store.ElementHidingDatabase
import com.duckduckgo.elementhiding.store.ElementHidingRepository
import com.duckduckgo.elementhiding.store.RealElementHidingRepository
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object ElementHidingModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideElementHidingDatabase(context: Context): ElementHidingDatabase {
        return Room.databaseBuilder(context, ElementHidingDatabase::class.java, "element_hiding.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideElementHidingRepository(
        database: ElementHidingDatabase,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        @IsMainProcess isMainProcess: Boolean,
    ): ElementHidingRepository {
        return RealElementHidingRepository(database, appCoroutineScope, dispatcherProvider, isMainProcess)
    }
}
