

package com.duckduckgo.privacy.config.internal.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface DevPrivacyConfigSettingsDataStore {
    var remotePrivacyConfigUrl: String?
    var useCustomPrivacyConfigUrl: Boolean
    var canUrlBeChanged: Boolean
}

@ContributesBinding(AppScope::class)
class DevPrivacyConfigSettingsDataStoreImpl @Inject constructor(private val context: Context) : DevPrivacyConfigSettingsDataStore {

    override var remotePrivacyConfigUrl: String?
        get() = selectedRemotePrivacyConfigUrlSavedValue()
        set(value) = preferences.edit { putString(KEY_SELECTED_REMOTE_PRIVACY_CONFIG, value) }

    override var useCustomPrivacyConfigUrl: Boolean
        get() = preferences.getBoolean(KEY_CUSTOM_REMOTE_PRIVACY_CONFIG_ENABLED, false)
        set(enabled) = preferences.edit { putBoolean(KEY_CUSTOM_REMOTE_PRIVACY_CONFIG_ENABLED, enabled) }

    override var canUrlBeChanged: Boolean
        get() = preferences.getBoolean(KEY_URL_CHANGED, false)
        set(enabled) = preferences.edit { putBoolean(KEY_URL_CHANGED, enabled) }

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    private fun selectedRemotePrivacyConfigUrlSavedValue(): String? {
        return preferences.getString(KEY_SELECTED_REMOTE_PRIVACY_CONFIG, null)
    }

    companion object {
        private const val FILENAME = "com.duckduckgo.privacy.config.internal.settings"
        private const val KEY_SELECTED_REMOTE_PRIVACY_CONFIG = "KEY_SELECTED_REMOTE_PRIVACY_CONFIG"
        private const val KEY_CUSTOM_REMOTE_PRIVACY_CONFIG_ENABLED = "KEY_CUSTOM_REMOTE_PRIVACY_CONFIG_ENABLED"
        private const val KEY_URL_CHANGED = "KEY_URL_CHANGED"
    }
}
