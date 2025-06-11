

package com.duckduckgo.app.accessibility.di

import android.content.Context
import com.duckduckgo.app.accessibility.data.AccessibilitySettingsDataStore
import com.duckduckgo.app.accessibility.data.AccessibilitySettingsSharedPreferences
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
class AccessibilityModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesAccessibilitySettingsDataStore(
        context: Context,
        dispatcherProvider: DispatcherProvider,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
    ): AccessibilitySettingsDataStore = AccessibilitySettingsSharedPreferences(context, dispatcherProvider, appCoroutineScope)
}
