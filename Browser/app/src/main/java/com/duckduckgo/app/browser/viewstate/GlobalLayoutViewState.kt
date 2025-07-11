

package com.duckduckgo.app.browser.viewstate

sealed class GlobalLayoutViewState {
    data class Browser(val isNewTabState: Boolean = true) : GlobalLayoutViewState()
    object Invalidated : GlobalLayoutViewState()
}
