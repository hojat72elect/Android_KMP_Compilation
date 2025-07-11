

package com.duckduckgo.widget

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

interface WidgetPreferences {
    fun widgetTheme(widgetId: Int): WidgetTheme
    fun saveWidgetSelectedTheme(
        widgetId: Int,
        theme: String,
    )

    fun widgetSize(widgetId: Int): Pair<Int, Int>
    fun storeWidgetSize(
        widgetId: Int,
        columns: Int,
        rows: Int,
    )

    fun removeWidgetSettings(widgetId: Int)
}

class AppWidgetThemePreferences @Inject constructor(private val context: Context) : WidgetPreferences {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override fun widgetTheme(widgetId: Int): WidgetTheme {
        return WidgetTheme.valueOf(
            preferences.getString(keyForWidgetTheme(widgetId), WidgetTheme.SYSTEM_DEFAULT.toString()) ?: WidgetTheme.SYSTEM_DEFAULT.toString(),
        )
    }

    override fun saveWidgetSelectedTheme(
        widgetId: Int,
        theme: String,
    ) {
        preferences.edit(true) {
            putString(keyForWidgetTheme(widgetId), theme)
        }
    }

    override fun widgetSize(widgetId: Int): Pair<Int, Int> {
        return Pair(
            preferences.getInt("$SHARED_PREFS_WIDTH_KEY-$widgetId", 2),
            preferences.getInt("$SHARED_PREFS_HEIGHT_KEY-$widgetId", 2),
        )
    }

    override fun storeWidgetSize(
        widgetId: Int,
        columns: Int,
        rows: Int,
    ) {
        preferences.edit(true) {
            putInt("$SHARED_PREFS_WIDTH_KEY-$widgetId", columns)
            putInt("$SHARED_PREFS_HEIGHT_KEY-$widgetId", rows)
        }
    }

    override fun removeWidgetSettings(widgetId: Int) {
        preferences.edit(true) {
            remove("$SHARED_PREFS_WIDTH_KEY-$widgetId")
            remove("$SHARED_PREFS_HEIGHT_KEY-$widgetId")
            remove("$SHARED_PREFS_THEME_KEY-$widgetId")
        }
    }

    private fun keyForWidgetTheme(widgetId: Int): String {
        return "$SHARED_PREFS_THEME_KEY-$widgetId"
    }

    companion object {
        const val FILENAME = "com.duckduckgo.app.widget.theme"
        const val SHARED_PREFS_THEME_KEY = "SelectedTheme"
        const val SHARED_PREFS_WIDTH_KEY = "Width"
        const val SHARED_PREFS_HEIGHT_KEY = "Height"
    }
}
