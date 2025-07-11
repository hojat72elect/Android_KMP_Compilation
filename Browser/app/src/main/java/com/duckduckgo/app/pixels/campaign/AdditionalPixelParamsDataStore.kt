

package com.duckduckgo.app.pixels.campaign

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AdditionalPixelParamsDataStore {
    /**
     * Returns a list of eligible origins for the pixel parameter feature
     */
    var includedOrigins: List<String>
}

@ContributesBinding(AppScope::class)
class RealAdditionalPixelParamsDataStore @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : AdditionalPixelParamsDataStore {

    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(
            FILENAME,
            multiprocess = true,
            migrate = false,
        )
    }

    override var includedOrigins: List<String>
        get() = preferences.getString(KEY_INCLUDED_ORIGINS, null)?.run {
            this.split(SEPARATOR)
        } ?: emptyList()
        set(value) {
            preferences.edit(commit = true) {
                val stringValue = if (value.isNotEmpty()) {
                    value.joinToString(SEPARATOR)
                } else {
                    null
                }
                putString(KEY_INCLUDED_ORIGINS, stringValue)
            }
        }

    companion object {
        private const val SEPARATOR = ";"
        private const val KEY_INCLUDED_ORIGINS = "KEY_INCLUDED_ORIGINS"
        private const val FILENAME = "com.duckduckgo.ppro.promo.store"
    }
}
