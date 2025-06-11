

package com.duckduckgo.autofill.impl.service.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

interface AutofillServiceStore {
    suspend fun isDefaultAutofillProvider(): Boolean
    suspend fun updateDefaultAutofillProvider(isDefault: Boolean)
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealAutofillServiceStore @Inject constructor(
    @AutofillServiceDataStore private val store: DataStore<Preferences>,
) : AutofillServiceStore {

    override suspend fun isDefaultAutofillProvider(): Boolean {
        return store.data.firstOrNull()?.get(enabledByUserKey) ?: false
    }

    override suspend fun updateDefaultAutofillProvider(isDefault: Boolean) {
        Timber.i("DDGAutofillService updating default autofill provider to $isDefault")
        store.edit {
            it[enabledByUserKey] = isDefault
        }
    }

    companion object {
        private val enabledByUserKey = booleanPreferencesKey("enabled_by_user_key")
    }
}
