

package com.duckduckgo.privacy.config.store.features.gpc

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

interface GpcDataStore {

    var gpcEnabled: Boolean
}

class GpcSharedPreferences constructor(private val context: Context) : GpcDataStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override var gpcEnabled: Boolean
        get() = preferences.getBoolean(KEY_GPC_ENABLED, true)
        set(enabled) = preferences.edit { putBoolean(KEY_GPC_ENABLED, enabled) }

    companion object {
        const val FILENAME = "com.duckduckgo.privacy.config.impl.features.gpc"
        const val KEY_GPC_ENABLED = "KEY_GPC_ENABLED"
    }
}
