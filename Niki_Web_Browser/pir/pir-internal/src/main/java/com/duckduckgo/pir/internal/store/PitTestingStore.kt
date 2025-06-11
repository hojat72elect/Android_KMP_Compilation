

package com.duckduckgo.pir.internal.store

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

/**
 * This class is not meant to stay after the PoC. This should only contain optional testing specific data.
 */
interface PitTestingStore {
    var testerId: String?
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealPitTestingStore @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : PitTestingStore {
    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(
            FILENAME,
            multiprocess = true,
            migrate = false,
        )
    }

    override var testerId: String?
        get() = preferences.getString(KEY_TESTER_ID, null)
        set(value) {
            preferences.edit {
                putString(KEY_TESTER_ID, value)
            }
        }

    companion object {
        private const val FILENAME = "com.duckduckgo.pir.testing.v1"
        private const val KEY_TESTER_ID = "KEY_TESTER_ID"
    }
}
