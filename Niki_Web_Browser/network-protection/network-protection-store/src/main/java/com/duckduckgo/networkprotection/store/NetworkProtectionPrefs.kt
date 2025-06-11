

package com.duckduckgo.networkprotection.store

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider

interface NetworkProtectionPrefs {
    fun putBoolean(
        key: String,
        value: Boolean,
    )

    fun getBoolean(
        key: String,
        default: Boolean,
    ): Boolean

    fun putInt(
        key: String,
        value: Int,
    )

    fun getInt(
        key: String,
        default: Int,
    ): Int

    fun putString(
        key: String,
        value: String?,
    )

    fun setStringSet(
        key: String,
        value: Set<String>,
    )

    fun getString(
        key: String,
        default: String?,
    ): String?

    fun putLong(
        key: String,
        value: Long,
    )

    fun getLong(
        key: String,
        default: Long,
    ): Long

    fun getStringSet(
        key: String,
        default: Set<String> = emptySet(),
    ): Set<String>

    /**
     * Deletes all content in the shared preference
     */
    fun clear()
}

class RealNetworkProtectionPrefs constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : NetworkProtectionPrefs {
    private val prefs: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(FILENAME, multiprocess = true, migrate = false)
    }

    override fun putString(
        key: String,
        value: String?,
    ) {
        prefs.edit { putString(key, value) }
    }

    override fun setStringSet(key: String, value: Set<String>) {
        prefs.edit { putStringSet(key, value) }
    }

    override fun getString(
        key: String,
        default: String?,
    ): String? = prefs.getString(key, default)

    override fun putLong(
        key: String,
        value: Long,
    ) {
        prefs.edit { putLong(key, value) }
    }

    override fun getLong(
        key: String,
        default: Long,
    ): Long = prefs.getLong(key, default)

    override fun getStringSet(key: String, default: Set<String>): Set<String> {
        val result = prefs.getStringSet(key, default) ?: default
        // ensure we never modify the set instance returned by the getStringSet call
        return result.toSet()
    }

    override fun clear() {
        prefs.edit { clear() }
    }

    override fun putBoolean(
        key: String,
        value: Boolean,
    ) {
        prefs.edit { putBoolean(key, value) }
    }

    override fun getBoolean(
        key: String,
        default: Boolean,
    ): Boolean = prefs.getBoolean(key, default)

    override fun putInt(
        key: String,
        value: Int,
    ) {
        prefs.edit { putInt(key, value) }
    }

    override fun getInt(
        key: String,
        default: Int,
    ): Int = prefs.getInt(key, default)

    companion object {
        private const val FILENAME = "com.duckduckgo.networkprotection.store.prefs.v1"
    }
}
