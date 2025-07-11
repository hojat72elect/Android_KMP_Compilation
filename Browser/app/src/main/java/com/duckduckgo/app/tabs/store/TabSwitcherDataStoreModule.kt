

package com.duckduckgo.app.tabs.store

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
object TabSwitcherDataStoreModule {

    private val Context.tabSwitcherDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "tab_switcher",
    )

    @Provides
    @TabSwitcher
    fun tabSwitcherDataStore(context: Context): DataStore<Preferences> = context.tabSwitcherDataStore
}

@Qualifier
annotation class TabSwitcher
