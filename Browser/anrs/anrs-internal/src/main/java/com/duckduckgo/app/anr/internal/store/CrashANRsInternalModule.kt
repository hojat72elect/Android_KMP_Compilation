

package com.duckduckgo.app.anr.internal.store

import android.content.Context
import androidx.room.Room
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
class CrashANRsInternalModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAnrDatabase(context: Context): CrashANRsInternalDatabase {
        return Room.databaseBuilder(context, CrashANRsInternalDatabase::class.java, "crash_anr_internal_database.db")
            .addMigrations(*CrashANRsInternalDatabase.ALL_MIGRATIONS)
            .fallbackToDestructiveMigration()
            .build()
    }
}
