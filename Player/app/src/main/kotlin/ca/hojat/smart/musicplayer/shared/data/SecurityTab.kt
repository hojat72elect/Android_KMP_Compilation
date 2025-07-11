package ca.hojat.smart.musicplayer.shared.data

import androidx.biometric.auth.AuthPromptHost
import ca.hojat.smart.musicplayer.shared.ui.views.MyScrollView


interface SecurityTab {
    fun initTab(
        requiredHash: String,
        listener: HashListener,
        scrollView: MyScrollView,
        biometricPromptHost: AuthPromptHost,
        showBiometricAuthentication: Boolean
    )

    fun visibilityChanged(isVisible: Boolean)
}
