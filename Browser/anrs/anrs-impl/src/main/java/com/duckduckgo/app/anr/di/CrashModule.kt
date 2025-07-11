

package com.duckduckgo.app.anr.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.anrs.store.CrashDatabase
import com.duckduckgo.app.anrs.store.UncaughtExceptionDao
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi

@Module
@ContributesTo(AppScope::class)
object CrashModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    @InternalApi
    fun provideCrashDatabase(context: Context): CrashDatabase {
        return Room.databaseBuilder(context, CrashDatabase::class.java, "crash_database.db")
            .addMigrations(*CrashDatabase.ALL_MIGRATIONS.toTypedArray())
            .fallbackToDestructiveMigration()
            .enableMultiInstanceInvalidation()
            .build()
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideUncaughtExceptionDao(@InternalApi crashDatabase: CrashDatabase): UncaughtExceptionDao {
        return crashDatabase.uncaughtExceptionDao()
    }
}
