

package com.duckduckgo.networkprotection.impl.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface NetpVpnSettingsDataStore {
    var customDns: String?
}

@ContributesBinding(AppScope::class)
class RealNetpVpnSettingsDataStore @Inject constructor(sharedPreferencesProvider: SharedPreferencesProvider) : NetpVpnSettingsDataStore {

    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(FILENAME, multiprocess = true, migrate = false)
    }

    override var customDns: String?
        get() = preferences.getString(KEY_NETP_CUSTOM_DNS, null)
        set(value) = preferences.edit { putString(KEY_NETP_CUSTOM_DNS, value) }

    companion object {
        private const val FILENAME = "com.duckduckgo.networkprotection.env.store.v1"
        private const val KEY_NETP_CUSTOM_DNS = "KEY_NETP_CUSTOM_DNS"
    }
}
