

package com.duckduckgo.app.di

import android.content.Context
import com.duckduckgo.app.widget.ui.AppWidgetCapabilities
import com.duckduckgo.app.widget.ui.WidgetCapabilities
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.widget.SearchAndFavoritesGridCalculator
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
object WidgetModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun widgetCapabilities(context: Context): WidgetCapabilities = AppWidgetCapabilities(context)

    @Provides
    fun gridCalculator(): SearchAndFavoritesGridCalculator = SearchAndFavoritesGridCalculator()
}
