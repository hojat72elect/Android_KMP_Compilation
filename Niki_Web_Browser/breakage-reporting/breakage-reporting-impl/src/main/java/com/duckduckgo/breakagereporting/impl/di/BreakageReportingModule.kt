

package com.duckduckgo.breakagereporting.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.breakagereporting.impl.ALL_MIGRATIONS
import com.duckduckgo.breakagereporting.impl.BreakageReportingDatabase
import com.duckduckgo.breakagereporting.impl.BreakageReportingRepository
import com.duckduckgo.breakagereporting.impl.RealBreakageReportingRepository
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object BreakageReportingModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideBreakageReportingDatabase(context: Context): BreakageReportingDatabase {
        return Room.databaseBuilder(context, BreakageReportingDatabase::class.java, "breakage_reporting.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideBreakageReportingRepository(
        database: BreakageReportingDatabase,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        @IsMainProcess isMainProcess: Boolean,
    ): BreakageReportingRepository {
        return RealBreakageReportingRepository(database, appCoroutineScope, dispatcherProvider, isMainProcess)
    }
}
