

package com.duckduckgo.newtabpage.api

import android.content.Context
import android.view.View
import com.duckduckgo.common.utils.plugins.ActivePlugin

/**
 * This class is used to provide one of the two different version of NewTabPage
 * Legacy -> What existed before https://app.asana.com/0/1174433894299346/1207064372575037
 * New -> Implementation of https://app.asana.com/0/1174433894299346/1207064372575037
 */
interface NewTabPagePlugin : ActivePlugin {

    /**
     * This method returns a [View] that will be used as the NewTabPage content
     * @return [View]
     */
    fun getView(context: Context): View

    companion object {
        const val PRIORITY_LEGACY_NTP = 0
        const val PRIORITY_NTP = 100
    }
}
