

package com.duckduckgo.app.widget.ui

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.duckduckgo.widget.SearchAndFavoritesWidget
import com.duckduckgo.widget.SearchWidget
import com.duckduckgo.widget.SearchWidgetLight
import javax.inject.Inject

interface WidgetCapabilities {
    val supportsAutomaticWidgetAdd: Boolean
    val hasInstalledWidgets: Boolean
}

class AppWidgetCapabilities @Inject constructor(
    private val context: Context,
) : WidgetCapabilities {

    override val supportsAutomaticWidgetAdd: Boolean
        get() = AppWidgetManager.getInstance(context).isRequestPinAppWidgetSupported

    override val hasInstalledWidgets: Boolean
        get() = context.hasInstalledWidgets
}

val Context.hasInstalledWidgets: Boolean
    get() {
        val manager = AppWidgetManager.getInstance(this)
        val hasDarkWidget = manager.getAppWidgetIds(ComponentName(this, SearchWidget::class.java)).any()
        val hasLightWidget = manager.getAppWidgetIds(ComponentName(this, SearchWidgetLight::class.java)).any()
        val hasSearchAndFavoritesWidget = manager.getAppWidgetIds(ComponentName(this, SearchAndFavoritesWidget::class.java)).any()
        return hasDarkWidget || hasLightWidget || hasSearchAndFavoritesWidget
    }
