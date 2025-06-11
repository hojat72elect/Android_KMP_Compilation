

package com.duckduckgo.user.agent.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.user.agent.impl.store.ALL_MIGRATIONS
import com.duckduckgo.user.agent.impl.store.ClientBrandHintDatabase
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
class ClientBrandHintModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideClientHintDatabase(context: Context): ClientBrandHintDatabase {
        return Room.databaseBuilder(context, ClientBrandHintDatabase::class.java, "clientbrandhints.db")
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }
}
