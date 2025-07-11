

package com.duckduckgo.autofill.impl.ui.credential.management.importpassword.desktopapp

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

@ContributesTo(AppScope::class)
@Module
object ImportPasswordsViaDesktopSyncDataStoreModule {

    private val Context.importPasswordsDesktopSyncStore: DataStore<Preferences> by preferencesDataStore(
        name = "import_passwords_via_desktop_sync",
    )

    @Provides
    @SingleInstanceIn(AppScope::class)
    @ImportPasswordDesktopSync
    fun provideImportPasswordsDesktopSyncDataStore(context: Context): DataStore<Preferences> {
        return context.importPasswordsDesktopSyncStore
    }
}

@Qualifier
annotation class ImportPasswordDesktopSync
