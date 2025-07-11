

package com.duckduckgo.cookies.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.cookies.api.CookiesFeatureName

interface CookiesFeatureToggleStore {
    fun deleteAll()

    fun get(
        featureName: CookiesFeatureName,
        defaultValue: Boolean,
    ): Boolean

    fun getMinSupportedVersion(featureName: CookiesFeatureName): Int

    fun insert(toggle: CookiesFeatureToggles)
}

class RealCookiesFeatureToggleStore(private val context: Context) : CookiesFeatureToggleStore {
    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override fun deleteAll() {
        preferences.edit().clear().apply()
    }

    override fun get(featureName: CookiesFeatureName, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(featureName.value, defaultValue)
    }

    override fun getMinSupportedVersion(featureName: CookiesFeatureName): Int {
        return preferences.getInt("${featureName.value}$MIN_SUPPORTED_VERSION", 0)
    }

    override fun insert(toggle: CookiesFeatureToggles) {
        preferences.edit {
            putBoolean(toggle.featureName.value, toggle.enabled)
            toggle.minSupportedVersion?.let {
                putInt("${toggle.featureName.value}$MIN_SUPPORTED_VERSION", it)
            }
        }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.cookies.store.toggles"
        const val MIN_SUPPORTED_VERSION = "MinSupportedVersion"
    }
}

data class CookiesFeatureToggles(
    val featureName: CookiesFeatureName,
    val enabled: Boolean,
    val minSupportedVersion: Int?,
)
