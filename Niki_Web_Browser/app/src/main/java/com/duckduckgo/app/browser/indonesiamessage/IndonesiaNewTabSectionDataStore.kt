

package com.duckduckgo.app.browser.indonesiamessage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.duckduckgo.app.browser.di.IndonesiaNewTabSection
import com.duckduckgo.app.browser.indonesiamessage.RealIndonesiaNewTabSectionDataStore.Keys.MESSAGE_DISMISSED
import com.duckduckgo.app.browser.indonesiamessage.RealIndonesiaNewTabSectionDataStore.Keys.SHOW_MESSAGE
import com.duckduckgo.app.browser.indonesiamessage.RealIndonesiaNewTabSectionDataStore.Keys.SHOW_MESSAGE_COUNT
import com.duckduckgo.app.browser.indonesiamessage.RealIndonesiaNewTabSectionDataStore.Keys.SHOW_MESSAGE_TIMESTAMP
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface IndonesiaNewTabSectionDataStore {

    val showMessage: Flow<Boolean>

    suspend fun updateShowMessage(maxCount: Int)

    suspend fun dismissMessage()
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealIndonesiaNewTabSectionDataStore @Inject constructor(
    @IndonesiaNewTabSection private val store: DataStore<Preferences>,
) : IndonesiaNewTabSectionDataStore {

    override val showMessage: Flow<Boolean>
        get() = store.data
            .map { prefs ->
                prefs[SHOW_MESSAGE] ?: false
            }
            .distinctUntilChanged()

    override suspend fun updateShowMessage(maxCount: Int) {
        val now = Instant.now().toEpochMilli()

        val count = store.data.map { prefs -> prefs[SHOW_MESSAGE_COUNT] ?: 0 }
            .distinctUntilChanged().first()
        val timestamp = store.data.map { prefs -> prefs[SHOW_MESSAGE_TIMESTAMP] ?: 0L }
            .distinctUntilChanged().first()

        if (count >= maxCount && now >= timestamp) {
            store.edit { prefs ->
                prefs[SHOW_MESSAGE] = false
            }
            return
        }

        val shouldShowMessage = (now > timestamp)
        store.edit { prefs ->
            if (shouldShowMessage) {
                prefs[SHOW_MESSAGE_TIMESTAMP] = now.plus(TimeUnit.HOURS.toMillis(INTERVAL_HOURS))
                prefs[SHOW_MESSAGE_COUNT] = count + 1
                prefs[SHOW_MESSAGE] = true
                prefs[MESSAGE_DISMISSED] = false
            } else if (prefs[MESSAGE_DISMISSED] == true) {
                prefs[SHOW_MESSAGE] = false
            }
        }
    }

    override suspend fun dismissMessage() {
        store.edit { prefs ->
            prefs[MESSAGE_DISMISSED] = true
        }
    }

    internal object Keys {
        val SHOW_MESSAGE = booleanPreferencesKey(name = "show_message")
        val SHOW_MESSAGE_TIMESTAMP = longPreferencesKey(name = "show_message_timestamp")
        val SHOW_MESSAGE_COUNT = intPreferencesKey(name = "show_message_count")
        val MESSAGE_DISMISSED = booleanPreferencesKey(name = "message_dismissed")
    }

    companion object {
        const val INTERVAL_HOURS = 24L
    }
}
