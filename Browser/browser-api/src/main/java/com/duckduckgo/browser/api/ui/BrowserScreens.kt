

package com.duckduckgo.browser.api.ui

import com.duckduckgo.navigation.api.GlobalActivityStarter

/**
 * Model that represents the Browser Screens hosted inside :app module that can be launched from other modules.
 */
sealed class BrowserScreens {

    /**
     * Use this model to launch the standalone WebView
     */
    data class WebViewActivityWithParams(
        val url: String,
        val screenTitle: String,
        val supportNewWindows: Boolean = false,
    ) : GlobalActivityStarter.ActivityParams

    /**
     * Use this model to launch the Feedback screen
     */
    object FeedbackActivityWithEmptyParams : GlobalActivityStarter.ActivityParams

    /**
     * Use this model to launch the Bookmarks screen
     */
    object BookmarksScreenNoParams : GlobalActivityStarter.ActivityParams

    /**
     * Use this model to launch the Settings screen
     */
    object SettingsScreenNoParams : GlobalActivityStarter.ActivityParams

    /**
     * Use this model to launch the New Tab Settings screen
     */
    object NewTabSettingsScreenNoParams : GlobalActivityStarter.ActivityParams
}
