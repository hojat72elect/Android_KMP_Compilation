package ca.hojat.notes.niki.shared.views

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.provider.Settings
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.biometric.auth.AuthPromptHost
import com.github.ajalt.reprint.core.AuthenticationFailureReason
import com.github.ajalt.reprint.core.AuthenticationListener
import com.github.ajalt.reprint.core.Reprint
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.databinding.TabFingerprintBinding
import ca.hojat.notes.niki.shared.extensions.applyColorFilter
import ca.hojat.notes.niki.shared.extensions.beGoneIf
import ca.hojat.notes.niki.shared.extensions.getProperTextColor
import ca.hojat.notes.niki.shared.extensions.toast
import ca.hojat.notes.niki.shared.extensions.updateTextColors
import ca.hojat.notes.niki.shared.helpers.PROTECTION_FINGERPRINT
import ca.hojat.notes.niki.shared.interfaces.HashListener
import ca.hojat.notes.niki.shared.interfaces.SecurityTab

class FingerprintTab(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs),
    SecurityTab {

    private val registerHandler = Handler()

    lateinit var hashListener: HashListener

    private lateinit var binding: TabFingerprintBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = TabFingerprintBinding.bind(this)
        val textColor = context.getProperTextColor()
        context.updateTextColors(binding.fingerprintLockHolder)
        binding.fingerprintImage.applyColorFilter(textColor)

        binding.fingerprintSettings.setOnClickListener {
            context.startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    override fun initTab(
        requiredHash: String,
        listener: HashListener,
        scrollView: MyScrollView,
        biometricPromptHost: AuthPromptHost,
        showBiometricAuthentication: Boolean
    ) {
        hashListener = listener
    }

    override fun visibilityChanged(isVisible: Boolean) {
        if (isVisible) {
            checkRegisteredFingerprints()
        } else {
            Reprint.cancelAuthentication()
        }
    }

    private fun checkRegisteredFingerprints() {
        val hasFingerprints = Reprint.hasFingerprintRegistered()
        binding.fingerprintSettings.beGoneIf(hasFingerprints)
        binding.fingerprintLabel.text =
            context.getString(if (hasFingerprints) R.string.place_finger else R.string.no_fingerprints_registered)

        Reprint.authenticate(object : AuthenticationListener {
            override fun onSuccess(moduleTag: Int) {
                hashListener.receivedHash("", PROTECTION_FINGERPRINT)
            }

            override fun onFailure(
                failureReason: AuthenticationFailureReason?,
                fatal: Boolean,
                errorMessage: CharSequence?,
                moduleTag: Int,
                errorCode: Int
            ) {
                when (failureReason) {
                    AuthenticationFailureReason.AUTHENTICATION_FAILED -> context.toast(R.string.authentication_failed)
                    AuthenticationFailureReason.LOCKED_OUT -> context.toast(R.string.authentication_blocked)
                    else -> {}
                }
            }
        })

        registerHandler.postDelayed({
            checkRegisteredFingerprints()
        }, RECHECK_PERIOD)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        registerHandler.removeCallbacksAndMessages(null)
        Reprint.cancelAuthentication()
    }

    companion object {
        private const val RECHECK_PERIOD = 3000L
    }
}
