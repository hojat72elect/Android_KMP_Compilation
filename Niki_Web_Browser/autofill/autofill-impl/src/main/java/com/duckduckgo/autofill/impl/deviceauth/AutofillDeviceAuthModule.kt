

package com.duckduckgo.autofill.impl.deviceauth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import javax.inject.Qualifier

@Module
@ContributesTo(AppScope::class)
class AutofillDeviceAuthModule {

    private val Context.autofillDeviceAuthDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "autofill_device_auth_store",
    )

    @Provides
    @SingleInstanceIn(AppScope::class)
    @AutofillDeviceAuthStore
    fun providesDeviceAuthDataStore(context: Context): DataStore<Preferences> {
        return context.autofillDeviceAuthDataStore
    }
}

@Qualifier
annotation class AutofillDeviceAuthStore
