

package com.duckduckgo.app.browser

import android.view.View
import android.view.animation.AccelerateInterpolator
import com.duckduckgo.common.ui.view.gone
import com.duckduckgo.common.ui.view.show
import timber.log.Timber

class HomeBackgroundLogo(private var ddgLogoView: View) {

    private var readyToShowLogo = false

    fun showLogo() {
        this.readyToShowLogo = true
        update()
    }

    fun hideLogo() {
        readyToShowLogo = false
        update()
    }

    private fun update() {
        if (readyToShowLogo) {
            fadeLogoIn()
        } else {
            fadeLogoOut()
        }
    }

    private fun fadeLogoIn() {
        Timber.v("showLogo")
        // To avoid glitches when calling show/hide logo within a small amount of time we keep this 50ms animation
        ddgLogoView.animate().apply {
            duration = FADE_IN_DURATION
            interpolator = AccelerateInterpolator()
            alpha(1f)
            ddgLogoView.show()
        }
    }

    private fun fadeLogoOut() {
        Timber.v("hideLogo")
        ddgLogoView.gone()
    }

    companion object {
        private const val FADE_IN_DURATION = 50L
    }
}
