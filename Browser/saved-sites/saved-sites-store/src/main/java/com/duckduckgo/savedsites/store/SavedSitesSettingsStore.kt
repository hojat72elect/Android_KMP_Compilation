

package com.duckduckgo.savedsites.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.savedsites.store.FavoritesDisplayMode.NATIVE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

interface SavedSitesSettingsStore {
    var favoritesDisplayMode: FavoritesDisplayMode
    fun favoritesFormFactorModeFlow(): Flow<FavoritesDisplayMode>
}

class SavedSitesSettingsSharedPrefStore(
    private val context: Context,
    private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : SavedSitesSettingsStore {

    private val favoritesFavoritesDisplayModeFlow = MutableSharedFlow<FavoritesDisplayMode>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    }

    init {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            favoritesFavoritesDisplayModeFlow.emit(favoritesDisplayMode)
        }
    }

    override fun favoritesFormFactorModeFlow(): Flow<FavoritesDisplayMode> = favoritesFavoritesDisplayModeFlow

    override var favoritesDisplayMode: FavoritesDisplayMode
        get() {
            val storedValue = preferences.getString(
                KEY_FAVORITES_DISPLAY_MODE,
                NATIVE.value,
            )
            return FavoritesDisplayMode.values().firstOrNull { it.value == storedValue } ?: NATIVE
        }
        set(favouritesDisplayMode) {
            preferences.edit(commit = true) {
                putString(KEY_FAVORITES_DISPLAY_MODE, favouritesDisplayMode.value)
            }
            emitNewValue()
        }

    private fun emitNewValue() {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            favoritesFavoritesDisplayModeFlow.emit(favoritesDisplayMode)
        }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.savedsites.settings"
        const val KEY_FAVORITES_DISPLAY_MODE = "KEY_FAVORITES_DISPLAY_MODE"
    }
}

enum class FavoritesDisplayMode(val value: String) {
    NATIVE("display_native"),
    UNIFIED("display_all"),
}
