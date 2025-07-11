

package com.duckduckgo.daxprompts.impl.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.duckduckgo.daxprompts.impl.di.DaxPrompts
import com.duckduckgo.daxprompts.impl.store.SharedPreferencesDaxPromptsDataStore.Keys.DAX_PROMPTS_SHOW_BROWSER_COMPARISON
import com.duckduckgo.daxprompts.impl.store.SharedPreferencesDaxPromptsDataStore.Keys.DAX_PROMPTS_SHOW_DUCK_PLAYER
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

interface DaxPromptsDataStore {
    suspend fun setDaxPromptsShowDuckPlayer(show: Boolean)
    suspend fun getDaxPromptsShowDuckPlayer(): Boolean
    suspend fun setDaxPromptsShowBrowserComparison(show: Boolean)
    suspend fun getDaxPromptsShowBrowserComparison(): Boolean
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class SharedPreferencesDaxPromptsDataStore @Inject constructor(
    @DaxPrompts private val store: DataStore<Preferences>,
) : DaxPromptsDataStore {

    private object Keys {
        val DAX_PROMPTS_SHOW_DUCK_PLAYER = booleanPreferencesKey(name = "DAX_PROMPTS_SHOW_DUCK_PLAYER")
        val DAX_PROMPTS_SHOW_BROWSER_COMPARISON = booleanPreferencesKey(name = "DAX_PROMPTS_SHOW_BROWSER_COMPARISON")
    }

    override suspend fun setDaxPromptsShowDuckPlayer(show: Boolean) {
        store.edit { it[DAX_PROMPTS_SHOW_DUCK_PLAYER] = show }
    }

    override suspend fun getDaxPromptsShowDuckPlayer(): Boolean {
        return store.data.firstOrNull()?.get(DAX_PROMPTS_SHOW_DUCK_PLAYER) ?: true
    }

    override suspend fun setDaxPromptsShowBrowserComparison(show: Boolean) {
        store.edit { it[DAX_PROMPTS_SHOW_BROWSER_COMPARISON] = show }
    }

    override suspend fun getDaxPromptsShowBrowserComparison(): Boolean {
        return store.data.firstOrNull()?.get(DAX_PROMPTS_SHOW_BROWSER_COMPARISON) ?: true
    }
}
