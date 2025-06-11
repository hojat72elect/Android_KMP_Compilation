

package com.duckduckgo.newtabpage.api

import android.content.Context
import android.view.View
import com.duckduckgo.common.utils.plugins.ActivePlugin

/**
 * This class is used to provide each of the Sections that build the New Tab Page
 * Implementation of https://app.asana.com/0/1174433894299346/12070643725750
 */
interface NewTabPageSectionPlugin : ActivePlugin {

    /** Name of the focused view version */
    val name: String

    /**
     * This method returns a [View] that will be used as the NewTabPage content
     * @return [View]
     */
    fun getView(context: Context): View?

    /**
     * This method returns a [Boolean] that shows if the plugin is enabled manually by the user
     * @return [Boolean]
     */
    suspend fun isUserEnabled(): Boolean

    companion object {
        const val PRIORITY_INDONESIA_MESSAGE = 5
        const val PRIORITY_REMOTE_MESSAGE = 10
        const val PRIORITY_APP_TP = 20
        const val PRIORITY_FAVOURITES = 33
        const val PRIORITY_SHORTCUTS = 40
    }
}

enum class NewTabPageSection {
    INDONESIA_MESSAGE,
    APP_TRACKING_PROTECTION,
    REMOTE_MESSAGING_FRAMEWORK,
    FAVOURITES,
    SHORTCUTS,
}
