

package com.duckduckgo.app.accessibility

import com.duckduckgo.navigation.api.GlobalActivityStarter.ActivityParams

sealed class AccessibilityScreens : ActivityParams {
    /**
     * Use this model to launch the Accessibility screen
     */
    data object Default : AccessibilityScreens() {
        private fun readResolve(): Any = Default
    }

    /**
     * Use this model to launch the Accessibility screen with a highlighted item
     */
    data class HighlightedItem(val highlightedItem: String) : AccessibilityScreens()
}
