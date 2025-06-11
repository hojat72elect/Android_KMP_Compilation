

package com.duckduckgo.sync.settings.impl

import android.content.Context
import androidx.room.Room
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
class StoreModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideSettingsDatabase(context: Context): SettingsDatabase {
        return Room.databaseBuilder(context, SettingsDatabase::class.java, "settings.db")
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideSettingsSyncMetadataDao(database: SettingsDatabase): SettingsSyncMetadataDao {
        return database.settingsSyncDao()
    }
}
