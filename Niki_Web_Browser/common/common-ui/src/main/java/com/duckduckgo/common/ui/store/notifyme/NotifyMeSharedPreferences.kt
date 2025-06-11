

package com.duckduckgo.common.ui.store.notifyme

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class NotifyMeSharedPreferences @Inject constructor(
    private val context: Context,
) : NotifyMeDataStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override fun isComponentDismissed(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    override fun setComponentDismissed(key: String) {
        preferences.edit {
            putBoolean(key, true)
        }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.mobile.android.ui.store.notifyme.prefs"
    }
}
