

package com.duckduckgo.app.autocomplete.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AutoCompleteDataStore {
    suspend fun setHistoryInAutoCompleteIAMDismissed()

    suspend fun wasHistoryInAutoCompleteIAMDismissed(): Boolean

    suspend fun incrementCountHistoryInAutoCompleteIAMShown()

    suspend fun countHistoryInAutoCompleteIAMShown(): Int
}

@ContributesBinding(AppScope::class)
class SharedPreferencesAutoCompleteDataStore @Inject constructor(
    private val context: Context,
) : AutoCompleteDataStore {
    companion object {
        const val FILENAME = "com.duckduckgo.app.autocomplete"
        const val KEY_HISTORY_AUTOCOMPLETE_SHOW_COUNT = "KEY_HISTORY_AUTOCOMPLETE_SHOW_COUNT"
        const val KEY_HISTORY_AUTOCOMPLETE_DISMISSED = "KEY_HISTORY_AUTOCOMPLETE_DISMISSED"
    }

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override suspend fun countHistoryInAutoCompleteIAMShown(): Int {
        return preferences.getInt(KEY_HISTORY_AUTOCOMPLETE_SHOW_COUNT, 0)
    }

    override suspend fun incrementCountHistoryInAutoCompleteIAMShown() {
        updateValue(KEY_HISTORY_AUTOCOMPLETE_SHOW_COUNT, countHistoryInAutoCompleteIAMShown() + 1)
    }
    override suspend fun setHistoryInAutoCompleteIAMDismissed() {
        updateValue(KEY_HISTORY_AUTOCOMPLETE_DISMISSED, true)
    }

    override suspend fun wasHistoryInAutoCompleteIAMDismissed(): Boolean {
        return preferences.getBoolean(KEY_HISTORY_AUTOCOMPLETE_DISMISSED, false)
    }

    private fun updateValue(
        key: String,
        value: Int,
    ) {
        preferences.edit(true) {
            putInt(key, value)
        }
    }

    private fun updateValue(
        key: String,
        value: Boolean,
    ) {
        preferences.edit(true) {
            putBoolean(key, value)
        }
    }
}
