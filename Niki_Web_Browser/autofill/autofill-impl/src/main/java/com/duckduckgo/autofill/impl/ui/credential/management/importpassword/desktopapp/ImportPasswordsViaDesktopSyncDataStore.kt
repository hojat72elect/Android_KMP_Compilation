

package com.duckduckgo.autofill.impl.ui.credential.management.importpassword.desktopapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

interface ImportPasswordsViaDesktopSyncDataStore {

    suspend fun startUserJourney()
    suspend fun getUserJourneyStartTime(): Long?
    suspend fun clearUserJourneyStartTime()
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class ImportPasswordsViaDesktopSyncDataStoreImpl @Inject constructor(
    @ImportPasswordDesktopSync private val store: DataStore<Preferences>,
) : ImportPasswordsViaDesktopSyncDataStore {

    private val userJourneyStartTime = longPreferencesKey("user_journey_started_timestamp")

    override suspend fun startUserJourney() {
        store.edit {
            it[userJourneyStartTime] = System.currentTimeMillis()
        }
    }

    override suspend fun getUserJourneyStartTime(): Long? {
        return store.data.firstOrNull()?.get(userJourneyStartTime)
    }

    override suspend fun clearUserJourneyStartTime() {
        store.edit {
            it.remove(userJourneyStartTime)
        }
    }
}
