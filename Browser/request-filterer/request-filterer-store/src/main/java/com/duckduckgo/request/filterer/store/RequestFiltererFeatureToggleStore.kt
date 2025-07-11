

package com.duckduckgo.request.filterer.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.request.filterer.api.RequestFiltererFeatureName

interface RequestFiltererFeatureToggleStore {
    fun deleteAll()

    fun get(
        featureName: RequestFiltererFeatureName,
        defaultValue: Boolean,
    ): Boolean

    fun getMinSupportedVersion(featureName: RequestFiltererFeatureName): Int

    fun insert(toggle: RequestFiltererFeatureToggles)
}

class RealRequestFiltererFeatureToggleStore(private val context: Context) : RequestFiltererFeatureToggleStore {
    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override fun deleteAll() {
        preferences.edit().clear().apply()
    }

    override fun get(featureName: RequestFiltererFeatureName, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(featureName.value, defaultValue)
    }

    override fun getMinSupportedVersion(featureName: RequestFiltererFeatureName): Int {
        return preferences.getInt("${featureName.value}$MIN_SUPPORTED_VERSION", 0)
    }

    override fun insert(toggle: RequestFiltererFeatureToggles) {
        preferences.edit {
            putBoolean(toggle.featureName.value, toggle.enabled)
            toggle.minSupportedVersion?.let {
                putInt("${toggle.featureName.value}$MIN_SUPPORTED_VERSION", it)
            }
        }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.request.filterer.store.toggles"
        const val MIN_SUPPORTED_VERSION = "MinSupportedVersion"
    }
}

data class RequestFiltererFeatureToggles(
    val featureName: RequestFiltererFeatureName,
    val enabled: Boolean,
    val minSupportedVersion: Int?,
)
