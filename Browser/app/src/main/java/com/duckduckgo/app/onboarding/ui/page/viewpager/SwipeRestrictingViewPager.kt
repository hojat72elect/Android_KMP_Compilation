

package com.duckduckgo.app.onboarding.ui.page.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SwipeRestrictingViewPager(
    context: Context,
    attrs: AttributeSet,
) : ViewPager(context, attrs) {

    private var swipingEnabled: Boolean = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return swipingEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return swipingEnabled && super.onInterceptTouchEvent(event)
    }

    fun setSwipingEnabled(enabled: Boolean) {
        this.swipingEnabled = enabled
    }
}
