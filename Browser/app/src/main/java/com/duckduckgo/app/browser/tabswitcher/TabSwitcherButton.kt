

package com.duckduckgo.app.browser.tabswitcher

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.duckduckgo.app.browser.R

open class TabSwitcherButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    open var count: Int = 0

    open var hasUnread = false

    internal val tabCount: FrameLayout by lazy { findViewById(R.id.tabCount) }

    fun increment(callback: () -> Unit) {
        fadeOutCount {
            count += 1
            fadeInCount()
            callback()
        }
    }

    private fun fadeOutCount(callback: () -> Unit) {
        val listener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // otherwise on end keeps being called repeatedly
                tabCount.animate().setListener(null)
                callback()
            }
        }

        tabCount.animate()
            .setDuration(300)
            .alpha(0.0f)
            .setListener(listener)
            .start()
    }

    private fun fadeInCount() {
        tabCount.animate()
            .setDuration(300)
            .alpha(1.0f)
            .start()
    }
}
