

package com.duckduckgo.duckchat.impl.di

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
object DuckChatDataStoreModule {

    private val Context.duckChatDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "duck_chat",
    )

    @Provides
    @DuckChat
    fun provideDuckChatDataStore(context: Context): DataStore<Preferences> = context.duckChatDataStore
}

@Qualifier
internal annotation class DuckChat
