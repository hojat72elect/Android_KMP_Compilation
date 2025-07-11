

package com.duckduckgo.common.ui.store

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.duckduckgo.common.ui.DuckDuckGoTheme
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

interface AppTheme {
    fun isLightModeEnabled(): Boolean
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class BrowserAppTheme @Inject constructor(
    private val context: Context,
    private val themeDataStore: ThemingDataStore,
) : AppTheme {

    override fun isLightModeEnabled(): Boolean {
        val lightModeEnabled = when (themeDataStore.theme) {
            DuckDuckGoTheme.LIGHT -> true
            DuckDuckGoTheme.EXPERIMENT_LIGHT -> true
            DuckDuckGoTheme.DARK -> false
            DuckDuckGoTheme.EXPERIMENT_DARK -> false
            DuckDuckGoTheme.SYSTEM_DEFAULT -> {
                !isNightMode(context)
            }
        }
        Log.d("Trackers", "isLightModeEnabled for ${themeDataStore.theme} $lightModeEnabled")
        return lightModeEnabled
    }

    private fun isNightMode(context: Context): Boolean {
        val nightModeMasked = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeMasked == Configuration.UI_MODE_NIGHT_YES
    }
}
