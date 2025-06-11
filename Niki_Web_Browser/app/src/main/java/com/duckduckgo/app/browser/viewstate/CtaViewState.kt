

package com.duckduckgo.app.browser.viewstate

import com.duckduckgo.app.cta.ui.Cta

data class CtaViewState(
    val cta: Cta? = null,
    val isOnboardingCompleteInNewTabPage: Boolean = false,
    val isBrowserShowing: Boolean = true,
    val isErrorShowing: Boolean = false,
)
