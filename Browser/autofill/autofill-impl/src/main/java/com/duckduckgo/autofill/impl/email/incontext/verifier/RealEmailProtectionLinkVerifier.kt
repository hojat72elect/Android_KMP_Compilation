

package com.duckduckgo.autofill.impl.email.incontext.verifier

import com.duckduckgo.autofill.api.emailprotection.EmailProtectionLinkVerifier
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealEmailProtectionLinkVerifier @Inject constructor() : EmailProtectionLinkVerifier {

    override fun shouldDelegateToInContextView(url: String?, inContextViewAlreadyShowing: Boolean?): Boolean {
        if (inContextViewAlreadyShowing != true) {
            return false
        }

        return url.isEmailProtectionLink()
    }

    private fun String?.isEmailProtectionLink(): Boolean {
        return this?.contains(VERIFICATION_URL) == true
    }

    companion object {
        private const val VERIFICATION_URL = "https://duckduckgo.com/email/login?otp"
    }
}
