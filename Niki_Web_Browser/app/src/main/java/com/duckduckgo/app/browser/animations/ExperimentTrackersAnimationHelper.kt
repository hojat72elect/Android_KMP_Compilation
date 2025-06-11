

package com.duckduckgo.app.browser.animations

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.duckduckgo.common.ui.view.text.DaxTextView

interface ExperimentTrackersAnimationHelper {

    fun startShieldPopAnimation(
        omnibarShieldAnimationView: LottieAnimationView,
        trackersCountAndBlockedViews: List<DaxTextView>,
        omnibarTextInput: View,
    )

    fun cancelAnimations()
}
