

package com.duckduckgo.app.tabs.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.duckduckgo.app.tabs.model.TabSwitcherData
import com.duckduckgo.app.tabs.model.TabSwitcherData.LayoutType
import com.duckduckgo.app.tabs.model.TabSwitcherData.UserState
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TabSwitcherDataStore {
    val data: Flow<TabSwitcherData>

    suspend fun setUserState(userState: UserState)
    suspend fun setTabLayoutType(layoutType: LayoutType)
    fun isAnimationTileDismissed(): Flow<Boolean>
    suspend fun setIsAnimationTileDismissed(isDismissed: Boolean)
}

@ContributesBinding(AppScope::class)
class TabSwitcherPrefsDataStore @Inject constructor(
    @TabSwitcher private val store: DataStore<Preferences>,
) : TabSwitcherDataStore {
    companion object {
        const val KEY_USER_STATE = "KEY_USER_STATE"
        const val KEY_LAYOUT_TYPE = "KEY_LAYOUT_TYPE"
        const val KEY_IS_ANIMATION_TILE_DISMISSED = "KEY_IS_ANIMATION_TILE_DISMISSED"
    }

    override val data: Flow<TabSwitcherData> = store.data.map { preferences ->
        TabSwitcherData(
            userState = UserState.valueOf(preferences[stringPreferencesKey(KEY_USER_STATE)] ?: UserState.UNKNOWN.name),
            layoutType = LayoutType.valueOf(preferences[stringPreferencesKey(KEY_LAYOUT_TYPE)] ?: LayoutType.GRID.name),
        )
    }

    override suspend fun setUserState(userState: UserState) {
        store.edit { preferences ->
            preferences[stringPreferencesKey(KEY_USER_STATE)] = userState.name
        }
    }

    override suspend fun setTabLayoutType(layoutType: LayoutType) {
        store.edit { preferences ->
            preferences[stringPreferencesKey(KEY_LAYOUT_TYPE)] = layoutType.name
        }
    }

    override fun isAnimationTileDismissed(): Flow<Boolean> {
        return store.data.map { preferences ->
            preferences[booleanPreferencesKey(KEY_IS_ANIMATION_TILE_DISMISSED)] ?: false
        }
    }

    override suspend fun setIsAnimationTileDismissed(isDismissed: Boolean) {
        store.edit { preferences ->
            preferences[booleanPreferencesKey(KEY_IS_ANIMATION_TILE_DISMISSED)] = isDismissed
        }
    }
}
