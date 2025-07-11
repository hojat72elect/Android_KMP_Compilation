

package com.duckduckgo.app.browser

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import androidx.core.animation.addListener

class SmoothProgressAnimator(private val pageLoadingIndicator: ProgressBar) {

    private var progressBarAnimation: ObjectAnimator = ObjectAnimator.ofInt(pageLoadingIndicator, "progress", 0)

    fun onNewProgress(
        newProgress: Int,
        onAnimationEnd: (Animator?) -> Unit,
    ) {
        progressBarAnimation.pause()
        pageLoadingIndicator.apply {
            if (progress > newProgress) {
                progress = 0
            }
            progressBarAnimation.apply {
                removeAllListeners()
                setIntValues(newProgress)
                duration = if (newProgress < MIN_PROGRESS_BAR) ANIM_DURATION_PROGRESS_LONG else ANIM_DURATION_PROGRESS_SHORT
                interpolator = AccelerateDecelerateInterpolator()
                addListener(onEnd = onAnimationEnd)
                start()
            }
        }
    }

    companion object {
        private const val MIN_PROGRESS_BAR = 75
        private const val ANIM_DURATION_PROGRESS_LONG = 1500L
        private const val ANIM_DURATION_PROGRESS_SHORT = 200L
    }
}
