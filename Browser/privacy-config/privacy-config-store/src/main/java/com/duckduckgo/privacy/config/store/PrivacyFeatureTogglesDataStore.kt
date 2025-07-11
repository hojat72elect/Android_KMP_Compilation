

package com.duckduckgo.privacy.config.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.privacy.config.api.PrivacyFeatureName

interface PrivacyFeatureTogglesDataStore {
    fun get(
        featureName: PrivacyFeatureName,
        defaultValue: Boolean,
    ): Boolean

    fun getMinSupportedVersion(featureName: PrivacyFeatureName): Int

    fun insert(toggle: PrivacyFeatureToggles)
    fun deleteAll()
}

class PrivacyFeatureTogglesSharedPreferences constructor(private val context: Context) :
    PrivacyFeatureTogglesDataStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override fun get(
        featureName: PrivacyFeatureName,
        defaultValue: Boolean,
    ): Boolean {
        return preferences.getBoolean(featureName.value, defaultValue)
    }

    override fun getMinSupportedVersion(featureName: PrivacyFeatureName): Int {
        return preferences.getInt("${featureName.value}$MIN_SUPPORTED_VERSION", 0)
    }

    override fun insert(toggle: PrivacyFeatureToggles) {
        preferences.edit {
            putBoolean(toggle.featureName, toggle.enabled)
            toggle.minSupportedVersion?.let {
                putInt("${toggle.featureName}$MIN_SUPPORTED_VERSION", it)
            }
        }
    }

    override fun deleteAll() {
        preferences.edit().clear().apply()
    }

    companion object {
        const val FILENAME = "com.duckduckgo.privacy.config.store.toggles"
        const val MIN_SUPPORTED_VERSION = "MinSupportedVersion"
    }
}

data class PrivacyFeatureToggles(
    val featureName: String,
    val enabled: Boolean,
    val minSupportedVersion: Int?,
)
