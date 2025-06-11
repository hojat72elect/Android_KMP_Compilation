

package com.duckduckgo.networkprotection.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.ui.AppBreakageCategory
import com.duckduckgo.networkprotection.impl.R
import com.duckduckgo.networkprotection.store.NetPGeoswitchingRepository
import com.duckduckgo.networkprotection.store.NetPManualExclusionListRepository
import com.duckduckgo.networkprotection.store.NetpDataStore
import com.duckduckgo.networkprotection.store.NetpDataStoreSharedPreferences
import com.duckduckgo.networkprotection.store.NetworkProtectionPrefs
import com.duckduckgo.networkprotection.store.RealNetPGeoswitchingRepository
import com.duckduckgo.networkprotection.store.RealNetPManualExclusionListRepository
import com.duckduckgo.networkprotection.store.RealNetworkProtectionPrefs
import com.duckduckgo.networkprotection.store.db.AutoExcludeDao
import com.duckduckgo.networkprotection.store.db.NetPDatabase
import com.duckduckgo.networkprotection.store.remote_config.NetPConfigTogglesDao
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object DataModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideNetworkProtectionRepository(
        sharedPreferencesProvider: SharedPreferencesProvider,
    ): NetworkProtectionPrefs = RealNetworkProtectionPrefs(sharedPreferencesProvider)

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun bindNetPDatabase(context: Context): NetPDatabase {
        return Room.databaseBuilder(context, NetPDatabase::class.java, "vpn-netp.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .addMigrations(*NetPDatabase.ALL_MIGRATIONS.toTypedArray())
            .build()
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideNetPManualExclusionListRepository(
        database: NetPDatabase,
    ): NetPManualExclusionListRepository {
        return RealNetPManualExclusionListRepository(database.exclusionListDao())
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideNetPGeoswitchingRepository(
        database: NetPDatabase,
        networkProtectionPrefs: NetworkProtectionPrefs,
        dispatcherProvider: DispatcherProvider,
    ): NetPGeoswitchingRepository {
        return RealNetPGeoswitchingRepository(networkProtectionPrefs, database.geoswitchingDao(), dispatcherProvider)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideAutoExcludeDao(netpDatabase: NetPDatabase): AutoExcludeDao {
        return netpDatabase.autoExcludeDao()
    }
}

@Module
@ContributesTo(AppScope::class)
object NetPBreakageCategoriesModule {
    @Provides
    @NetpBreakageCategories
    fun provideAppTrackerBreakageCategories(context: Context): List<AppBreakageCategory> {
        return mutableListOf(
            AppBreakageCategory("crashes", context.getString(R.string.netpReportBreakageCategoryCrashes)),
            AppBreakageCategory("messages", context.getString(R.string.netpReportBreakageCategoryMessages)),
            AppBreakageCategory("calls", context.getString(R.string.netpReportBreakageCategoryCalls)),
            AppBreakageCategory("content", context.getString(R.string.netpReportBreakageCategoryContent)),
            AppBreakageCategory("connection", context.getString(R.string.netpReportBreakageCategoryConnection)),
            AppBreakageCategory("iot", context.getString(R.string.netpReportBreakageCategoryIot)),
        ).apply {
            shuffle()
            add(AppBreakageCategory("featurerequest", context.getString(R.string.netpReportBreakageCategoryFeatureRequest)))
            add(AppBreakageCategory("other", context.getString(R.string.netpReportBreakageCategoryOther)))
        }
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    @ProdNetPConfigTogglesDao
    fun provideNetPConfigTogglesDao(netpDatabase: NetPDatabase): NetPConfigTogglesDao {
        return netpDatabase.configTogglesDao()
    }
}

@Module
@ContributesTo(AppScope::class)
object NetPDataStoreModule {
    @Provides
    fun provideNetPDataStore(
        sharedPreferencesProvider: SharedPreferencesProvider,
    ): NetpDataStore = NetpDataStoreSharedPreferences(sharedPreferencesProvider)
}
