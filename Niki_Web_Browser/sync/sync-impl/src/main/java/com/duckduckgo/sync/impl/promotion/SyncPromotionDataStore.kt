

package com.duckduckgo.sync.impl.promotion

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.impl.di.SyncPromotion
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

interface SyncPromotionDataStore {
    suspend fun hasBookmarksPromoBeenDismissed(): Boolean
    suspend fun recordBookmarksPromoDismissed()

    suspend fun hasPasswordsPromoBeenDismissed(): Boolean
    suspend fun recordPasswordsPromoDismissed()
}

@ContributesBinding(AppScope::class)
class SyncPromotionDataStoreImpl @Inject constructor(
    @SyncPromotion private val dataStore: DataStore<Preferences>,
) : SyncPromotionDataStore {

    override suspend fun hasBookmarksPromoBeenDismissed(): Boolean {
        return dataStore.data.map { it[bookmarksPromoDismissedKey] }.firstOrNull() != null
    }

    override suspend fun recordBookmarksPromoDismissed() {
        dataStore.edit { it[bookmarksPromoDismissedKey] = System.currentTimeMillis() }
    }

    override suspend fun hasPasswordsPromoBeenDismissed(): Boolean {
        return dataStore.data.map { it[passwordsPromoDismissedKey] }.firstOrNull() != null
    }

    override suspend fun recordPasswordsPromoDismissed() {
        dataStore.edit { it[passwordsPromoDismissedKey] = System.currentTimeMillis() }
    }

    companion object {
        private val bookmarksPromoDismissedKey = longPreferencesKey("bookmarks_promo_dismissed")
        private val passwordsPromoDismissedKey = longPreferencesKey("passwords_promo_dismissed")
    }
}
