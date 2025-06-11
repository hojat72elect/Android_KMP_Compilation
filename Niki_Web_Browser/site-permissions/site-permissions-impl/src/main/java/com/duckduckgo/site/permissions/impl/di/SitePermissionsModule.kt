

package com.duckduckgo.site.permissions.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.site.permissions.store.ALL_MIGRATIONS
import com.duckduckgo.site.permissions.store.SitePermissionsDatabase
import com.duckduckgo.site.permissions.store.SitePermissionsPreferences
import com.duckduckgo.site.permissions.store.SitePermissionsPreferencesImp
import com.duckduckgo.site.permissions.store.sitepermissions.SitePermissionsDao
import com.duckduckgo.site.permissions.store.sitepermissionsallowed.SitePermissionsAllowedDao
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object SitePermissionsModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesSitePermissionsDatabase(context: Context): SitePermissionsDatabase {
        return Room.databaseBuilder(context, SitePermissionsDatabase::class.java, "site_permissions.db")
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesSitePermissionsDao(sitePermissionsDatabase: SitePermissionsDatabase): SitePermissionsDao {
        return sitePermissionsDatabase.sitePermissionsDao()
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesSitePermissionsAllowedDao(sitePermissionsDatabase: SitePermissionsDatabase): SitePermissionsAllowedDao {
        return sitePermissionsDatabase.sitePermissionsAllowedDao()
    }

    @Provides
    fun providesSitePermissionsPreferences(context: Context): SitePermissionsPreferences {
        return SitePermissionsPreferencesImp(context)
    }
}
