

package com.duckduckgo.autofill.api

import com.duckduckgo.navigation.api.GlobalActivityStarter

/**
 * Launch params for starting In-Context Email Protection flow
 */
object EmailProtectionInContextSignUpScreenNoParams : GlobalActivityStarter.ActivityParams {
    private fun readResolve(): Any = EmailProtectionInContextSignUpScreenNoParams
}

/**
 * Launch params for resuming In-Context Email Protection flow from an email verification link
 */
data class EmailProtectionInContextSignUpHandleVerificationLink(val url: String) : GlobalActivityStarter.ActivityParams

/**
 * Activity result codes
 */
object EmailProtectionInContextSignUpScreenResult {
    const val SUCCESS = 1
    const val CANCELLED = 2
}
