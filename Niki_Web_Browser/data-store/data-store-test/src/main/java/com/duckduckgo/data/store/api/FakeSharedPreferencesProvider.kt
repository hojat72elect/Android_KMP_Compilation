

package com.duckduckgo.data.store.api

import android.content.SharedPreferences
import com.duckduckgo.common.test.api.InMemorySharedPreferences

class FakeSharedPreferencesProvider : SharedPreferencesProvider {
    override fun getSharedPreferences(name: String, multiprocess: Boolean, migrate: Boolean): SharedPreferences {
        return InMemorySharedPreferences()
    }

    override fun getEncryptedSharedPreferences(
        name: String,
        multiprocess: Boolean,
    ): SharedPreferences {
        return getSharedPreferences(name, multiprocess)
    }
}
