

package com.duckduckgo.app.appearance

import com.duckduckgo.navigation.api.GlobalActivityStarter.ActivityParams

/**
 * Use this model to launch the Appearance screen
 */
sealed class AppearanceScreen : ActivityParams {
    /**
     * Use this model to launch the Appearance screen
     */
    data object Default : AppearanceScreen() {
        private fun readResolve(): Any = Default
    }

    /**
     * Use this model to launch the Appearance screen with a highlighted item
     */
    data class HighlightedItem(val highlightedItem: String) : AppearanceScreen()
}
