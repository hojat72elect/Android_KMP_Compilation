

package com.duckduckgo.autofill.impl.service

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.duckduckgo.autofill.impl.deviceauth.AutofillDeviceAuthStore
import com.duckduckgo.autofill.impl.time.TimeProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

interface AutofillProviderDeviceAuth {
    suspend fun isAuthRequired(): Boolean
    suspend fun recordSuccessfulAuthorization()
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealAutofillProviderDeviceAuth @Inject constructor(
    private val timeProvider: TimeProvider,
    @AutofillDeviceAuthStore private val store: DataStore<Preferences>,
) : AutofillProviderDeviceAuth {

    private val lastAuthTimeKey = longPreferencesKey("last_auth_time_key")

    override suspend fun isAuthRequired(): Boolean {
        val lastAuthTime = store.data.firstOrNull()?.get(lastAuthTimeKey) ?: 0L
        if (lastAuthTime == 0L) return true

        val timeSinceLastAuth = timeProvider.currentTimeMillis() - lastAuthTime
        if (timeSinceLastAuth <= AUTH_GRACE_EXTENDED_PERIOD_MS) {
            Timber.v("Within grace period; auth not required")
            return false
        }
        return true
    }

    override suspend fun recordSuccessfulAuthorization() {
        store.edit {
            it[lastAuthTimeKey] = timeProvider.currentTimeMillis()
        }
    }

    companion object {
        private const val AUTH_GRACE_EXTENDED_PERIOD_MS = 180_000
    }
}
