

package com.duckduckgo.newtabpage.api

import android.content.Context
import com.duckduckgo.common.utils.plugins.ActivePlugin

/**
 * This class is used to provide each of the Shortcuts that build the Shortcut section in New Tab Page
 * Implementation of https://app.asana.com/0/1174433894299346/12070643725750
 */
interface NewTabPageShortcutPlugin : ActivePlugin {

    /**
     * This method returns a [NewTabShortcut] that will be used as the Shortcuts content
     * @return [NewTabShortcut]
     */
    fun getShortcut(): NewTabShortcut

    fun onClick(context: Context)

    /**
     * This method returns a [Boolean] that shows if the plugin is enabled manually by the user
     * @return [Boolean]
     */
    suspend fun isUserEnabled(): Boolean

    /**
     * Toggle shortcut visibility
     * Used from the New Tab Settings screen
     */
    suspend fun setUserEnabled(enabled: Boolean)

    companion object {
        const val PRIORITY_BOOKMARKS = 10
        const val PRIORITY_AUTOFILL = 20
        const val PRIORITY_DOWNLOADS = 30
        const val PRIORITY_SETTINGS = 40
        const val PRIORITY_AI_CHAT = 50
    }
}

interface NewTabShortcut {
    fun name(): String
    fun titleResource(): Int
    fun iconResource(): Int
}
