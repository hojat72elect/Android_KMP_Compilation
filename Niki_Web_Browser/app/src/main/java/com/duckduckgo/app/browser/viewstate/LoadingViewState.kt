

package com.duckduckgo.app.browser.viewstate

data class LoadingViewState(
    val isLoading: Boolean = false,
    val trackersAnimationEnabled: Boolean = true,
    val progress: Int = 0,
    val url: String = "",
)
