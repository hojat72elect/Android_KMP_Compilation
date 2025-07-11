

package com.duckduckgo.duckplayer.impl

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
object DuckPlayerDataStoreModule {

    private val Context.duckPlayerDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "duck_player",
    )

    @Provides
    @DuckPlayer
    fun provideDuckPlayerDataStore(context: Context): DataStore<Preferences> = context.duckPlayerDataStore
}

@Qualifier
internal annotation class DuckPlayer
