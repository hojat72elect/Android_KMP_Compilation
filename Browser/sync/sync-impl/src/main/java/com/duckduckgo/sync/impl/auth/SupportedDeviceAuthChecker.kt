

package com.duckduckgo.sync.impl.auth

import android.app.KeyguardManager
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface SupportedDeviceAuthChecker {
    fun supportsStrongAuthentication(): Boolean
    fun supportsLegacyAuthentication(): Boolean
}

@ContributesBinding(AppScope::class)
class RealSupportedDeviceAuthChecker @Inject constructor(
    private val context: Context,
) : SupportedDeviceAuthChecker {
    private val biometricManager: BiometricManager by lazy {
        BiometricManager.from(context)
    }

    private val keyguardManager: KeyguardManager by lazy {
        context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    }

    override fun supportsStrongAuthentication(): Boolean =
        biometricManager.canAuthenticate(Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL) == BIOMETRIC_SUCCESS

    override fun supportsLegacyAuthentication(): Boolean = keyguardManager.isDeviceSecure
}
