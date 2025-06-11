

package com.duckduckgo.sync.api

import android.content.*
import android.view.*

/**
 * Use this interface to create a new plugin that will be used to display a specific settings section
 */
interface SyncSettingsPlugin {
    /**
     * This method returns a [View] that will be used as a setting item
     * @return [View]
     */
    fun getView(context: Context): View
}
