

package com.duckduckgo.savedsites.impl.store

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
object BookmarksDataStoreModule {

    private val Context.bookmarksDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "bookmarks",
    )

    @Provides
    @Bookmarks
    fun provideBookmarksDataStore(context: Context): DataStore<Preferences> = context.bookmarksDataStore
}

@Qualifier
internal annotation class Bookmarks
