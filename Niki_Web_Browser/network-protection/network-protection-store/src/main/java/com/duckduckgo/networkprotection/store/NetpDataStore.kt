

package com.duckduckgo.networkprotection.store

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider

interface NetpDataStore {
    var authToken: String?
    var didAcceptedTerms: Boolean

    fun clear()
}

class NetpDataStoreSharedPreferences constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : NetpDataStore {

    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(
            FILENAME,
            multiprocess = true,
            migrate = false,
        )
    }

    override var authToken: String?
        get() = preferences.getString(KEY_AUTH_TOKEN, null)
        set(value) {
            preferences.edit(commit = true) {
                if (value == null) {
                    remove(KEY_AUTH_TOKEN)
                } else {
                    putString(KEY_AUTH_TOKEN, value)
                }
            }
        }
    override var didAcceptedTerms: Boolean
        get() = preferences.getBoolean(KEY_WAITLIST_ACCEPTED_TERMS, false)
        set(value) {
            preferences.edit(commit = true) {
                putBoolean(KEY_WAITLIST_ACCEPTED_TERMS, value)
            }
        }

    override fun clear() {
        preferences.edit { clear() }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.netp.store.waitlist"
        const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
        const val KEY_WAITLIST_ACCEPTED_TERMS = "KEY_WAITLIST_ACCEPTED_TERMS"
    }
}
