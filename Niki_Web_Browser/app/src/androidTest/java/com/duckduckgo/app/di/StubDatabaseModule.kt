

package com.duckduckgo.app.di

import android.content.Context
import android.webkit.WebViewDatabase
import androidx.room.Room
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module(includes = [DaoModule::class])
@ContributesTo(
    scope = AppScope::class,
    replaces = [DatabaseModule::class],
)
class StubDatabaseModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideWebviewDatabase(context: Context): WebViewDatabase {
        return WebViewDatabase.getInstance(context)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideDatabase(context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}
