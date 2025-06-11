

package com.duckduckgo.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface WidgetUpdater {
    fun updateWidgets(context: Context)
}

@ContributesBinding(AppScope::class)
class WidgetUpdaterImpl @Inject constructor() : WidgetUpdater {

    override fun updateWidgets(context: Context) {
        AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, SearchWidget::class.java))?.also {
            broadcastUpdate(
                it,
                context,
                SearchWidget::class.java,
            )
        }

        AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, SearchWidgetLight::class.java))?.also {
            broadcastUpdate(
                it,
                context,
                SearchWidgetLight::class.java,
            )
        }

        AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, SearchAndFavoritesWidget::class.java))?.also {
            broadcastUpdate(
                it,
                context,
                SearchAndFavoritesWidget::class.java,
            )
        }
    }

    private fun broadcastUpdate(
        id: IntArray,
        context: Context,
        clazz: Class<*>,
    ) {
        val intent = Intent(context, clazz)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, id)
        context.sendBroadcast(intent)
    }
}
