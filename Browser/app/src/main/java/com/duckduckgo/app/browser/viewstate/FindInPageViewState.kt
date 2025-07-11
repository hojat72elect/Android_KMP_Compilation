

package com.duckduckgo.app.browser.viewstate

data class FindInPageViewState(
    val visible: Boolean = false,
    val showNumberMatches: Boolean = false,
    val activeMatchIndex: Int = 0,
    val searchTerm: String = "",
    val numberMatches: Int = 0,
)
