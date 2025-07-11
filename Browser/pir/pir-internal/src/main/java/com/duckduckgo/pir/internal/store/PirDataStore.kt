

package com.duckduckgo.pir.internal.store

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider

interface PirDataStore {
    var mainConfigEtag: String?
}

internal class RealPirDataStore(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : PirDataStore {
    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(
            FILENAME,
            multiprocess = true,
            migrate = false,
        )
    }
    override var mainConfigEtag: String?
        get() = preferences.getString(KEY_MAIN_ETAG, null)
        set(value) {
            preferences.edit {
                putString(KEY_MAIN_ETAG, value)
            }
        }

    companion object {
        private const val FILENAME = "com.duckduckgo.pir.v1"
        private const val KEY_MAIN_ETAG = "KEY_MAIN_ETAG"
    }
}
