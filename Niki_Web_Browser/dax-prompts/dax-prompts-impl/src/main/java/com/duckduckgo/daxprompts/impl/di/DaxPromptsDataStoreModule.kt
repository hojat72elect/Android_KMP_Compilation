

package com.duckduckgo.daxprompts.impl.di

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
object DaxPromptsDataStoreModule {

    private val Context.daxPromptsDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "dax_prompts",
    )

    @Provides
    @DaxPrompts
    fun provideDaxPromptsDataStore(context: Context): DataStore<Preferences> = context.daxPromptsDataStore
}

@Qualifier
internal annotation class DaxPrompts
