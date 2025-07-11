package com.simplemobiletools.smsmessenger.views

import android.content.Context
import android.util.AttributeSet
import androidx.biometric.auth.AuthPromptHost
import androidx.constraintlayout.widget.ConstraintLayout
import com.simplemobiletools.smsmessenger.extensions.showBiometricPrompt
import com.simplemobiletools.smsmessenger.databinding.TabBiometricIdBinding
import com.simplemobiletools.smsmessenger.extensions.getContrastColor
import com.simplemobiletools.smsmessenger.extensions.getProperPrimaryColor
import com.simplemobiletools.smsmessenger.extensions.isWhiteTheme
import com.simplemobiletools.smsmessenger.extensions.updateTextColors
import com.simplemobiletools.smsmessenger.helpers.DARK_GREY
import com.simplemobiletools.smsmessenger.interfaces.HashListener
import com.simplemobiletools.smsmessenger.interfaces.SecurityTab

class BiometricIdTab(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs),
    SecurityTab {
    private lateinit var hashListener: HashListener
    private lateinit var biometricPromptHost: AuthPromptHost
    private lateinit var binding: TabBiometricIdBinding
    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = TabBiometricIdBinding.bind(this)
        context.updateTextColors(binding.biometricLockHolder)
        val textColor = if (context.isWhiteTheme()) {
            DARK_GREY
        } else {
            context.getProperPrimaryColor().getContrastColor()
        }

        binding.openBiometricDialog.setTextColor(textColor)
        binding.openBiometricDialog.setOnClickListener {
            biometricPromptHost.activity?.showBiometricPrompt(successCallback = hashListener::receivedHash)
        }
    }

    override fun initTab(
        requiredHash: String,
        listener: HashListener,
        scrollView: MyScrollView,
        biometricPromptHost: AuthPromptHost,
        showBiometricAuthentication: Boolean
    ) {
        this.biometricPromptHost = biometricPromptHost
        hashListener = listener
        if (showBiometricAuthentication) {
            binding.openBiometricDialog.performClick()
        }
    }

    override fun visibilityChanged(isVisible: Boolean) {}
}
