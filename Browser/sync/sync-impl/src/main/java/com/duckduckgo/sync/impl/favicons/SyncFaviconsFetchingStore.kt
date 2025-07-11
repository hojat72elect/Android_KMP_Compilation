

package com.duckduckgo.sync.impl.favicons

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.sync.api.favicons.FaviconsFetchingStore

class SyncFaviconFetchingStore(
    context: Context,
) : FaviconsFetchingStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override var isFaviconsFetchingEnabled: Boolean
        get() = preferences.getBoolean(KEY_FAVICONS_FETCHING_ENABLED, false)
        set(enabled) = preferences.edit(commit = true) { putBoolean(KEY_FAVICONS_FETCHING_ENABLED, enabled) }

    override var promptShown: Boolean
        get() = preferences.getBoolean(KEY_FAVICONS_PROMPT_SHOWN, false)
        set(enabled) = preferences.edit(commit = true) { putBoolean(KEY_FAVICONS_PROMPT_SHOWN, enabled) }

    companion object {
        private const val FILENAME = "com.duckduckgo.favicons.store.v1"
        private const val KEY_FAVICONS_FETCHING_ENABLED = "KEY_FAVICONS_FETCHING_ENABLED"
        private const val KEY_FAVICONS_PROMPT_SHOWN = "KEY_FAVICONS_PROMPT_SHOWN"
    }
}
