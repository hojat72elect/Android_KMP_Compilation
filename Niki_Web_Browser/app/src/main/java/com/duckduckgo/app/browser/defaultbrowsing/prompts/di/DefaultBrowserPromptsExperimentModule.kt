

package com.duckduckgo.app.browser.defaultbrowsing.prompts.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@ContributesTo(AppScope::class)
@Module
object DefaultBrowserPromptsExperimentModule {

    private val Context.defaultBrowserPromptsDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "default_browser_prompts",
    )

    @Provides
    @DefaultBrowserPrompts
    fun providesDefaultBrowserPromptsDataStore(context: Context): DataStore<Preferences> = context.defaultBrowserPromptsDataStore

    @Provides
    fun providesExperimentAppUsageDao(database: AppDatabase) = database.experimentAppUsageDao()
}

@Qualifier
annotation class DefaultBrowserPrompts
