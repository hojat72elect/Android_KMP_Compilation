

package com.duckduckgo.savedsites.impl.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.savedsites.impl.store.SharedPreferencesBookmarksDataStore.Keys.SORTING_MODE
import com.duckduckgo.savedsites.impl.store.SortingMode.MANUAL
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface BookmarksDataStore {
    fun getSortingMode(): SortingMode
    suspend fun setSortingMode(value: SortingMode)
}

enum class SortingMode(val index: Int) {
    MANUAL(0),
    NAME(1),
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class SharedPreferencesBookmarksDataStore @Inject constructor(
    @Bookmarks private val store: DataStore<Preferences>,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
) : BookmarksDataStore {

    private object Keys {
        val SORTING_MODE = intPreferencesKey(name = "SORTING_MODE")
    }

    override fun getSortingMode(): SortingMode {
        return sortingMode.value
    }

    override suspend fun setSortingMode(value: SortingMode) {
        store.edit { prefs -> prefs[SORTING_MODE] = value.index }
    }

    private val sortingMode: StateFlow<SortingMode> = store.data
        .map { prefs ->
            val sortingMode = prefs[SORTING_MODE] ?: 0
            SortingMode.entries.getOrNull(sortingMode) ?: MANUAL
        }
        .distinctUntilChanged()
        .stateIn(appCoroutineScope, SharingStarted.Eagerly, MANUAL)
}
