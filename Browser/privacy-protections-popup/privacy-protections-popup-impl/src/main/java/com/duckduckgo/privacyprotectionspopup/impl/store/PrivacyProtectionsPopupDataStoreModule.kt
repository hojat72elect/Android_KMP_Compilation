

package com.duckduckgo.privacyprotectionspopup.impl.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@ContributesTo(AppScope::class)
@Module
object PrivacyProtectionsPopupDataStoreModule {

    private val Context.privacyProtectionsPopupDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "privacy_protections_popup",
    )

    @Provides
    @PrivacyProtectionsPopup
    fun providePrivacyProtectionsPopupDataStore(context: Context): DataStore<Preferences> = context.privacyProtectionsPopupDataStore
}

@Qualifier
annotation class PrivacyProtectionsPopup
