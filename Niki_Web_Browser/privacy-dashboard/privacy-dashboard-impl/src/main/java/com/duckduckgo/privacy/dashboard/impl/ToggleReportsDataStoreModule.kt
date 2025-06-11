

package com.duckduckgo.privacy.dashboard.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@ContributesTo(AppScope::class)
@Module
object ToggleReportsDataStoreModule {

    private val Context.toggleReportsDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "toggle_report",
    )

    @Provides
    @ToggleReports
    fun provideToggleReportsDataStore(context: Context): DataStore<Preferences> = context.toggleReportsDataStore
}

@Qualifier
annotation class ToggleReports
