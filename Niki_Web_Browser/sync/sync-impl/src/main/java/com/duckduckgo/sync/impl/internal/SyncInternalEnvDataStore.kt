

package com.duckduckgo.sync.impl.internal

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.sync.impl.SyncService.Companion.SYNC_DEV_ENVIRONMENT_URL
import com.duckduckgo.sync.impl.SyncService.Companion.SYNC_PROD_ENVIRONMENT_URL
import javax.inject.Inject

interface SyncInternalEnvDataStore {
    var syncEnvironmentUrl: String
    var useSyncDevEnvironment: Boolean
}

class AppSyncInternalEnvDataStore @Inject constructor(
    context: Context,
) : SyncInternalEnvDataStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override var syncEnvironmentUrl: String
        get() = preferences.getString(KEY_SYNC_ENVIRONMENT_URL, SYNC_PROD_ENVIRONMENT_URL)!!
        set(value) = preferences.edit { putString(KEY_SYNC_ENVIRONMENT_URL, value) }
    override var useSyncDevEnvironment: Boolean
        get() = preferences.getBoolean(KEY_SYNC_USE_PROD_ENVIRONMENT_URL, false)
        set(enabled) = preferences.edit {
            putBoolean(KEY_SYNC_USE_PROD_ENVIRONMENT_URL, enabled)
            if (enabled) {
                putString(KEY_SYNC_ENVIRONMENT_URL, SYNC_DEV_ENVIRONMENT_URL)
            } else {
                putString(KEY_SYNC_ENVIRONMENT_URL, SYNC_PROD_ENVIRONMENT_URL)
            }
        }

    companion object {
        private const val FILENAME = "com.duckduckgo.sync.internal.env.store.v1"
        private const val KEY_SYNC_ENVIRONMENT_URL = "KEY_SYNC_ENVIRONMENT_URL"
        private const val KEY_SYNC_USE_PROD_ENVIRONMENT_URL = "KEY_SYNC_USE_PROD_ENVIRONMENT_URL"
    }
}
