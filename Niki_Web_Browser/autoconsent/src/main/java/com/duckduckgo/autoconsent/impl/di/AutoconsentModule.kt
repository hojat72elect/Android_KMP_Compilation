package com.duckduckgo.autoconsent.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.autoconsent.impl.remoteconfig.AutoconsentFeature
import com.duckduckgo.autoconsent.impl.store.AutoconsentDatabase
import com.duckduckgo.autoconsent.impl.store.AutoconsentSettingsRepository
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@ContributesTo(AppScope::class)
@Module
object AutoconsentModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAutoconsentSettingsRepository(
        context: Context,
        autoconsentFeature: AutoconsentFeature,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
    ): AutoconsentSettingsRepository {
        return AutoconsentSettingsRepository.create(context, autoconsentFeature, appCoroutineScope, dispatcherProvider)
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAutoconsentDatabase(context: Context): AutoconsentDatabase {
        return Room.databaseBuilder(context, AutoconsentDatabase::class.java, "autoconsent.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
