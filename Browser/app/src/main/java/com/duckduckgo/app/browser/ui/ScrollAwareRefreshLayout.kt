

package com.duckduckgo.app.browser.ui

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ScrollAwareRefreshLayout(
    context: Context,
    attrs: AttributeSet?,
) : SwipeRefreshLayout(context, attrs) {

    private var canChildScrollUpCallback: (() -> Boolean)? = null

    fun setProgressViewStartOffset(start: Int) {
        mOriginalOffsetTop = start
    }

    override fun canChildScrollUp(): Boolean {
        return canChildScrollUpCallback?.invoke() ?: super.canChildScrollUp()
    }

    fun setCanChildScrollUpCallback(callback: () -> Boolean) {
        canChildScrollUpCallback = callback
    }

    fun removeCanChildScrollUpCallback() {
        canChildScrollUpCallback = null
    }
}
