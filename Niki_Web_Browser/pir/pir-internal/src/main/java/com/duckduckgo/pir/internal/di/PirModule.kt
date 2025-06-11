

package com.duckduckgo.pir.internal.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.common.utils.CurrentTimeProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.pir.internal.service.DbpService
import com.duckduckgo.pir.internal.store.PirDatabase
import com.duckduckgo.pir.internal.store.PirRepository
import com.duckduckgo.pir.internal.store.RealPirDataStore
import com.duckduckgo.pir.internal.store.RealPirRepository
import com.duckduckgo.pir.internal.store.db.BrokerDao
import com.duckduckgo.pir.internal.store.db.BrokerJsonDao
import com.duckduckgo.pir.internal.store.db.OptOutResultsDao
import com.duckduckgo.pir.internal.store.db.ScanLogDao
import com.duckduckgo.pir.internal.store.db.ScanResultsDao
import com.duckduckgo.pir.internal.store.db.UserProfileDao
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
class PirModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun bindPirDatabase(context: Context): PirDatabase {
        return Room.databaseBuilder(context, PirDatabase::class.java, "pir.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .addMigrations(*PirDatabase.ALL_MIGRATIONS.toTypedArray())
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideBrokerJsonDao(database: PirDatabase): BrokerJsonDao {
        return database.brokerJsonDao()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideBrokerDao(database: PirDatabase): BrokerDao {
        return database.brokerDao()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideScanResultsDao(database: PirDatabase): ScanResultsDao {
        return database.scanResultsDao()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideUserProfileDao(database: PirDatabase): UserProfileDao {
        return database.userProfileDao()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideScanLogDao(database: PirDatabase): ScanLogDao {
        return database.scanLogDao()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideOptOutResultsDao(database: PirDatabase): OptOutResultsDao {
        return database.optOutResultsDao()
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providePirRepository(
        sharedPreferencesProvider: SharedPreferencesProvider,
        dispatcherProvider: DispatcherProvider,
        brokerJsonDao: BrokerJsonDao,
        brokerDao: BrokerDao,
        scanResultsDao: ScanResultsDao,
        currentTimeProvider: CurrentTimeProvider,
        moshi: Moshi,
        userProfileDao: UserProfileDao,
        scanLogDao: ScanLogDao,
        dbpService: DbpService,
        outResultsDao: OptOutResultsDao,
    ): PirRepository = RealPirRepository(
        moshi,
        dispatcherProvider,
        RealPirDataStore(sharedPreferencesProvider),
        currentTimeProvider,
        brokerJsonDao,
        brokerDao,
        scanResultsDao,
        userProfileDao,
        scanLogDao,
        dbpService,
        outResultsDao,
    )
}
