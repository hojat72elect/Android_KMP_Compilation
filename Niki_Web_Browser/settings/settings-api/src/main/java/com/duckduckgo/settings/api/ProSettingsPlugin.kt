

package com.duckduckgo.settings.api

import android.content.Context
import android.view.View

/**
 * Use this interface to create a new plugin that will be used to display a specific settings section
 */
interface SettingsPlugin {
    /**
     * This method returns a [View] that will be used as a setting item
     * @return [View]
     */
    fun getView(context: Context): View
}

/**
 * This is the plugin for the subs settings
 */
interface ProSettingsPlugin : SettingsPlugin

/**
 * This is the plugin for Duck Player settings
 */
interface DuckPlayerSettingsPlugin : SettingsPlugin
