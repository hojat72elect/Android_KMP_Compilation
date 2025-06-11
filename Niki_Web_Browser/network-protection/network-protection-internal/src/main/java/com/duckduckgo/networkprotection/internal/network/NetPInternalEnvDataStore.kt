

package com.duckduckgo.networkprotection.internal.network

import android.content.SharedPreferences
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import javax.inject.Inject

class NetPInternalEnvDataStore @Inject constructor(
    sharedPreferencesProvider: SharedPreferencesProvider,
) {

    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(FILENAME, multiprocess = true, migrate = false)
    }

    fun overrideVpnStaging(endpoint: String?) {
        if (endpoint == null) {
            preferences.edit().remove(KEY_STAGING_ENVIRONMENT).apply()
        } else {
            preferences.edit().putString(KEY_STAGING_ENVIRONMENT, endpoint).apply()
        }
    }

    fun getVpnStagingEndpoint(): String {
        return preferences.getString(KEY_STAGING_ENVIRONMENT, DEFAULT_STAGING_ENVIRONMENT) ?: DEFAULT_STAGING_ENVIRONMENT
    }

    companion object {
        private const val FILENAME = "com.duckduckgo.netp.internal.env.store.v1"
        private const val KEY_STAGING_ENVIRONMENT = "KEY_STAGING_ENVIRONMENT"
        private const val DEFAULT_STAGING_ENVIRONMENT = "https://staging1.netp.duckduckgo.com"
    }
}
