

package com.duckduckgo.httpsupgrade.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.store.BinaryDataStore
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.httpsupgrade.api.HttpsEmbeddedDataPersister
import com.duckduckgo.httpsupgrade.api.HttpsUpgradeDataDownloader
import com.duckduckgo.httpsupgrade.api.HttpsUpgrader
import com.duckduckgo.httpsupgrade.impl.HttpsBloomFilterFactory
import com.duckduckgo.httpsupgrade.impl.HttpsBloomFilterFactoryImpl
import com.duckduckgo.httpsupgrade.impl.HttpsDataPersister
import com.duckduckgo.httpsupgrade.impl.HttpsUpgradeDataDownloaderImpl
import com.duckduckgo.httpsupgrade.impl.HttpsUpgradeService
import com.duckduckgo.httpsupgrade.store.HttpsBloomFilterSpecDao
import com.duckduckgo.httpsupgrade.store.HttpsFalsePositivesDao
import com.duckduckgo.httpsupgrade.store.HttpsUpgradeDatabase
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object HttpsModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAppDatabase(
        context: Context,
    ): HttpsUpgradeDatabase {
        return Room.databaseBuilder(context, HttpsUpgradeDatabase::class.java, "httpsupgrade.db")
            .addMigrations(*HttpsUpgradeDatabase.ALL_MIGRATIONS.toTypedArray())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesHttpsFalsePositivesDao(database: HttpsUpgradeDatabase): HttpsFalsePositivesDao = database.httpsFalsePositivesDao()

    @Provides
    fun provideHttpsBloomFilterSpecDao(database: HttpsUpgradeDatabase): HttpsBloomFilterSpecDao = database.httpsBloomFilterSpecDao()

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideHttpsBloomFilterFactory(
        dao: HttpsBloomFilterSpecDao,
        binaryDataStore: BinaryDataStore,
        httpsEmbeddedDataPersister: HttpsEmbeddedDataPersister,
        httpsDataPersister: HttpsDataPersister,
        pixel: Pixel,
        context: Context,
    ): HttpsBloomFilterFactory {
        return HttpsBloomFilterFactoryImpl(dao, binaryDataStore, httpsEmbeddedDataPersister, httpsDataPersister, pixel, context)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideHttpsUpgradeDataDownloader(
        service: HttpsUpgradeService,
        httpsUpgrader: HttpsUpgrader,
        dataPersister: HttpsDataPersister,
        bloomFalsePositivesDao: HttpsFalsePositivesDao,
    ): HttpsUpgradeDataDownloader {
        return HttpsUpgradeDataDownloaderImpl(service, httpsUpgrader, dataPersister, bloomFalsePositivesDao)
    }

    @Provides
    fun provideHttpsDataPersister(
        binaryDataStore: BinaryDataStore,
        httpsBloomSpecDao: HttpsBloomFilterSpecDao,
        httpsFalsePositivesDao: HttpsFalsePositivesDao,
        httpsUpgradeDatabase: HttpsUpgradeDatabase,
    ): HttpsDataPersister {
        return HttpsDataPersister(binaryDataStore, httpsBloomSpecDao, httpsFalsePositivesDao, httpsUpgradeDatabase)
    }
}
