

package com.duckduckgo.app.browser.viewstate

data class OmnibarViewState(
    val omnibarText: String = "",
    val isEditing: Boolean = false,
    val navigationChange: Boolean = false,
    val forceExpand: Boolean = true,
)
