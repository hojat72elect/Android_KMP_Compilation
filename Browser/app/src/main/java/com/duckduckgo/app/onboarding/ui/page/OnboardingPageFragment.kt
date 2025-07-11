

package com.duckduckgo.app.onboarding.ui.page

import androidx.annotation.LayoutRes
import com.duckduckgo.app.onboarding.ui.OnboardingActivity
import com.duckduckgo.common.ui.DuckDuckGoFragment

abstract class OnboardingPageFragment(@LayoutRes contentLayoutId: Int = 0) : DuckDuckGoFragment(contentLayoutId) {

    fun onContinuePressed() {
        when (activity) {
            is OnboardingActivity -> (activity as OnboardingActivity).onContinueClicked()
        }
    }

    fun onSkipPressed() {
        when (activity) {
            is OnboardingActivity -> (activity as OnboardingActivity).onSkipClicked()
        }
    }
}
