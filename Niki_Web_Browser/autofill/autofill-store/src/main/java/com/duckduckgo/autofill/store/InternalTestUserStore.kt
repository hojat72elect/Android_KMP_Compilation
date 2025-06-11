

package com.duckduckgo.autofill.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

interface InternalTestUserStore {
    var isVerifiedInternalTestUser: Boolean
}

class RealInternalTestUserStore constructor(
    private val context: Context,
) : InternalTestUserStore {
    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override var isVerifiedInternalTestUser: Boolean
        get() = preferences.getBoolean(KEY_IS_VERIFIED_INTERNAL_TEST_USER, false)
        set(value) {
            preferences.edit {
                putBoolean(KEY_IS_VERIFIED_INTERNAL_TEST_USER, value)
            }
        }

    companion object {
        private const val FILENAME = "com.duckduckgo.autofill.store.InternalTestUserStore"
        private const val KEY_IS_VERIFIED_INTERNAL_TEST_USER = "KEY_IS_VERIFIED_INTERNAL_TEST_USER"
    }
}
