

package com.duckduckgo.verifiedinstallation.certificate

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VerificationCheckBuildCertificate {
    fun builtWithVerifiedCertificate(): Boolean
}

@ContributesBinding(AppScope::class)
class VerificationCheckBuildCertificateImpl @Inject constructor(
    private val certificateHashExtractor: SigningCertificateHashExtractor,
) : VerificationCheckBuildCertificate {

    override fun builtWithVerifiedCertificate(): Boolean {
        val hash = certificateHashExtractor.sha256Hash()
        return PRODUCTION_SHA_256_HASH.equals(hash, ignoreCase = true)
    }

    companion object {
        const val PRODUCTION_SHA_256_HASH = "bb7bb31c573c46a1da7fc5c528a6acf432108456feec50810c7f33694eb3d2d4"
    }
}
