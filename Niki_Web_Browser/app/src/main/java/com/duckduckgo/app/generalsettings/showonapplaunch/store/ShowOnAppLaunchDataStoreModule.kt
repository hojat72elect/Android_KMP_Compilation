

package com.duckduckgo.app.generalsettings.showonapplaunch.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.duckduckgo.app.generalsettings.showonapplaunch.ShowOnAppLaunchUrlConverterImpl
import com.duckduckgo.app.generalsettings.showonapplaunch.UrlConverter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@ContributesTo(AppScope::class)
@Module
object ShowOnAppLaunchDataStoreModule {

    private val Context.showOnAppLaunchDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "show_on_app_launch",
    )

    @Provides
    @ShowOnAppLaunch
    fun showOnAppLaunchDataStore(context: Context): DataStore<Preferences> = context.showOnAppLaunchDataStore

    @Provides
    fun showOnAppLaunchUrlConverter(): UrlConverter = ShowOnAppLaunchUrlConverterImpl()
}

@Qualifier
annotation class ShowOnAppLaunch
