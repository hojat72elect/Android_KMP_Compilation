

package com.duckduckgo.history.impl.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

interface HistoryDataStore {
    suspend fun isHistoryUserEnabled(default: Boolean): Boolean

    suspend fun setHistoryUserEnabled(value: Boolean)
}

class SharedPreferencesHistoryDataStore
constructor(
    private val context: Context,
) : HistoryDataStore {
    companion object {
        const val FILENAME = "com.duckduckgo.history"
        const val KEY_HISTORY_USER_ENABLED = "KEY_HISTORY_USER_ENABLED"
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    }

    override suspend fun isHistoryUserEnabled(default: Boolean): Boolean {
        return preferences.getBoolean(KEY_HISTORY_USER_ENABLED, default)
    }

    override suspend fun setHistoryUserEnabled(value: Boolean) {
        updateValue(KEY_HISTORY_USER_ENABLED, value)
    }

    private fun updateValue(
        key: String,
        value: Boolean,
    ) {
        preferences.edit(true) { putBoolean(key, value) }
    }
}
