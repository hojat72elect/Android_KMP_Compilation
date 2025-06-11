

package com.duckduckgo.app.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duckduckgo.app.bookmarks.migration.AppDatabaseBookmarksMigrationCallbackProvider
import com.duckduckgo.app.browser.DefaultWebViewDatabaseProvider
import com.duckduckgo.app.browser.WebViewDatabaseProvider
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.app.global.db.MigrationsProvider
import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.appbuildconfig.api.*
import com.duckduckgo.di.scopes.AppScope
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module(includes = [DaoModule::class])
object DatabaseModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideWebViewDatabaseProvider(context: Context): WebViewDatabaseProvider {
        return DefaultWebViewDatabaseProvider(context)
    }

    @Provides
    fun provideAppDatabaseBookmarksMigrationCallbackProvider(
        appDatabase: Lazy<AppDatabase>,
        appBuildConfig: AppBuildConfig,
    ): AppDatabaseBookmarksMigrationCallbackProvider {
        return AppDatabaseBookmarksMigrationCallbackProvider(appDatabase, appBuildConfig)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAppDatabase(
        context: Context,
        migrationsProvider: MigrationsProvider,
        databaseBookmarksMigrationCallbackProvider: AppDatabaseBookmarksMigrationCallbackProvider,
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
            .addMigrations(*migrationsProvider.ALL_MIGRATIONS.toTypedArray())
            .addCallback(migrationsProvider.BOOKMARKS_DB_ON_CREATE)
            .addCallback(migrationsProvider.CHANGE_JOURNAL_ON_OPEN)
            .addCallback(databaseBookmarksMigrationCallbackProvider.provideCallbacks())
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }

    @Provides
    fun provideDatabaseMigrations(
        context: Context,
        settingsDataStore: SettingsDataStore,
    ): MigrationsProvider {
        return MigrationsProvider(context, settingsDataStore)
    }
}
