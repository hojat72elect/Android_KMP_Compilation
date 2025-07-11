

package com.duckduckgo.app.anr.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.anrs.store.AnrsDatabase
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object AnrModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAnrDatabase(context: Context): AnrsDatabase {
        return Room.databaseBuilder(context, AnrsDatabase::class.java, "anr_database.db")
            .addMigrations(*AnrsDatabase.ALL_MIGRATIONS.toTypedArray())
            .fallbackToDestructiveMigration()
            .build()
    }
}
