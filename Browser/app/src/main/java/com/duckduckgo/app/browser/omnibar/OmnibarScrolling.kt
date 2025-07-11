

package com.duckduckgo.app.browser.omnibar

import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.*
import javax.inject.Inject

class OmnibarScrolling @Inject constructor() {

    fun enableOmnibarScrolling(toolbarContainer: View) {
        updateScrollFlag(SCROLL_FLAG_SCROLL or SCROLL_FLAG_SNAP or SCROLL_FLAG_ENTER_ALWAYS, toolbarContainer)
    }

    fun disableOmnibarScrolling(toolbarContainer: View) {
        updateScrollFlag(0, toolbarContainer)
    }

    private fun updateScrollFlag(
        flags: Int,
        toolbarContainer: View,
    ) {
        val params = toolbarContainer.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = flags
        toolbarContainer.layoutParams = params
    }
}
