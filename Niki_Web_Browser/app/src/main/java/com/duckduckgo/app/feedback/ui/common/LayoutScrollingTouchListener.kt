

package com.duckduckgo.app.feedback.ui.common

import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView

class LayoutScrollingTouchListener(
    private val scrollView: ScrollView,
    private val desiredScrollPosition: Int,
) : View.OnTouchListener {

    override fun onTouch(
        v: View?,
        event: MotionEvent?,
    ): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            v?.performClick()
            scrollView.postDelayed({ scrollView.smoothScrollTo(0, desiredScrollPosition) }, POST_DELAY_MS)
        }
        return false
    }

    companion object {
        private const val POST_DELAY_MS = 300L
    }
}
