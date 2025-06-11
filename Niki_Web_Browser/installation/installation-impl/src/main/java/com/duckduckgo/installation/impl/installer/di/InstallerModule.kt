

package com.duckduckgo.installation.impl.installer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import javax.inject.Qualifier

@Module
@ContributesTo(AppScope::class)
object InstallerModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "com.duckduckgo.installation.impl.installer",
    )

    @Provides
    @SingleInstanceIn(AppScope::class)
    @InstallSourceFullPackageDataStore
    fun provideInstallSourceFullPackageDataStore(context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Qualifier
    annotation class InstallSourceFullPackageDataStore
}
