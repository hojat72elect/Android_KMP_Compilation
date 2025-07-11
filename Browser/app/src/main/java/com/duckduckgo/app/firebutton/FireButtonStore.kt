

package com.duckduckgo.app.firebutton

import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface FireButtonStore {
    val fireButttonUseCount: Int

    fun incrementFireButtonUseCount()
}

@ContributesBinding(AppScope::class)
class RealFireButtonStore @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : FireButtonStore {

    private val preferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(FILENAME)
    }

    override val fireButttonUseCount: Int
        get() = preferences.getInt(KEY_FIREBUTTON_USE_COUNT, 0)

    override fun incrementFireButtonUseCount() {
        val currentCount = fireButttonUseCount
        preferences.edit {
            putInt(KEY_FIREBUTTON_USE_COUNT, currentCount + 1)
        }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.app.firebutton"
        const val KEY_FIREBUTTON_USE_COUNT = "FIREBUTTON_USE_COUNT"
    }
}
