

package com.duckduckgo.common.ui.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.common.ui.DuckDuckGoTheme
import com.duckduckgo.common.ui.experiments.visual.store.VisualDesignExperimentDataStore
import com.duckduckgo.common.ui.isInNightMode
import javax.inject.Inject

class ThemingSharedPreferences @Inject constructor(
    private val context: Context,
    private val visualDesignExperimentDataStore: VisualDesignExperimentDataStore,
) : ThemingDataStore {

    private val themePrefMapper = ThemePrefsMapper()

    override var theme: DuckDuckGoTheme
        get() = selectedThemeSavedValue()
        set(theme) = preferences.edit { putString(KEY_THEME, themePrefMapper.prefValue(theme)) }

    override fun isCurrentlySelected(theme: DuckDuckGoTheme): Boolean {
        return selectedThemeSavedValue() == theme
    }

    private fun selectedThemeSavedValue(): DuckDuckGoTheme {
        val savedValue = preferences.getString(KEY_THEME, null)
        return themePrefMapper.themeFrom(
            savedValue,
            DuckDuckGoTheme.SYSTEM_DEFAULT,
            context.isInNightMode(),
            visualDesignExperimentDataStore.isExperimentEnabled.value,
        )
    }

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    private class ThemePrefsMapper {

        companion object {
            private const val THEME_LIGHT = "LIGHT"
            private const val THEME_DARK = "DARK"
            private const val THEME_SYSTEM_DEFAULT = "SYSTEM_DEFAULT"
        }

        fun prefValue(theme: DuckDuckGoTheme) =
            when (theme) {
                DuckDuckGoTheme.SYSTEM_DEFAULT -> THEME_SYSTEM_DEFAULT
                DuckDuckGoTheme.LIGHT -> THEME_LIGHT
                else -> THEME_DARK
            }

        fun themeFrom(
            value: String?,
            defValue: DuckDuckGoTheme,
            isInNightMode: Boolean,
            isExperimentEnabled: Boolean,
        ) =
            when (value) {
                THEME_LIGHT -> if (isExperimentEnabled) {
                    DuckDuckGoTheme.EXPERIMENT_LIGHT
                } else {
                    DuckDuckGoTheme.LIGHT
                }

                THEME_DARK -> if (isExperimentEnabled) {
                    DuckDuckGoTheme.EXPERIMENT_DARK
                } else {
                    DuckDuckGoTheme.DARK
                }

                else ->
                    if (isExperimentEnabled) {
                        if (isInNightMode) {
                            DuckDuckGoTheme.EXPERIMENT_DARK
                        } else {
                            DuckDuckGoTheme.EXPERIMENT_LIGHT
                        }
                    } else {
                        DuckDuckGoTheme.SYSTEM_DEFAULT
                    }
            }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.app.settings_activity.settings"
        const val KEY_THEME = "THEME"
    }
}
