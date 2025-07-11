

package com.duckduckgo.app.browser.omnibar.animations

import com.airbnb.lottie.LottieAnimationView
import com.duckduckgo.app.global.model.PrivacyShield

/** Public interface for the Privacy Shield Animation Helper */
interface PrivacyShieldAnimationHelper {

    /**
     * This method will setup into [holder] a LottieAnimation based on [PrivacyShield] state.
     */
    fun setAnimationView(holder: LottieAnimationView, privacyShield: PrivacyShield)
}
