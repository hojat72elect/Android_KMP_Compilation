

package com.duckduckgo.autofill.impl.reporting.remoteconfig

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
class AutofillSiteBreakageReportingModule {

    private val Context.autofillSiteBreakageReportingDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "autofill_site_breakage_reporting",
    )

    @Provides
    @SingleInstanceIn(AppScope::class)
    @AutofillSiteBreakageReporting
    fun provideImportPasswordsDesktopSyncDataStore(context: Context): DataStore<Preferences> {
        return context.autofillSiteBreakageReportingDataStore
    }
}

@Qualifier
annotation class AutofillSiteBreakageReporting
