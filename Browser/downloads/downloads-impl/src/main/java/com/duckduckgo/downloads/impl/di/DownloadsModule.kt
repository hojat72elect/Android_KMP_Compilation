

package com.duckduckgo.downloads.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.downloads.store.DownloadsDatabase
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
class DownloadsModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideDownloadsDatabase(context: Context): DownloadsDatabase {
        return Room.databaseBuilder(context, DownloadsDatabase::class.java, "downloads.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
