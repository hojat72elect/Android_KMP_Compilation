

package com.duckduckgo.autofill.impl.jsbridge.response

import com.duckduckgo.autofill.impl.domain.javascript.JavascriptCredentials

data class ContainingCredentials(
    val type: String = "getAutofillDataResponse",
    val success: CredentialSuccessResponse,
) {

    data class CredentialSuccessResponse(
        val credentials: JavascriptCredentials,
        val action: String = "fill",
    )
}

data class AcceptGeneratedPasswordResponse(
    val type: String = "getAutofillDataResponse",
    val success: AcceptGeneratedPassword = AcceptGeneratedPassword(),
) {

    data class AcceptGeneratedPassword(val action: String = "acceptGeneratedPassword")
}

data class RejectGeneratedPasswordResponse(
    val type: String = "getAutofillDataResponse",
    val success: RejectGeneratedPassword = RejectGeneratedPassword(),
) {
    data class RejectGeneratedPassword(val action: String = "rejectGeneratedPassword")
}

data class EmptyResponse(
    val type: String = "getAutofillDataResponse",
    val success: EmptyCredentialResponse,
) {

    data class EmptyCredentialResponse(
        val action: String = "none",
    )
}

data class AvailableInputSuccessResponse(
    val credentials: AvailableInputTypeCredentials,
    val email: Boolean,
)

data class AvailableInputTypeCredentials(
    val username: Boolean,
    val password: Boolean,
)

data class EmailProtectionInContextSignupDismissedAtResponse(
    val type: String = "getIncontextSignupDismissedAt",
    val success: DismissedAt,
) {

    data class DismissedAt(val permanentlyDismissedAt: Long? = null, val isInstalledRecently: Boolean)
}

data class ShowInContextEmailProtectionSignupPromptResponse(
    val type: String = "ShowInContextEmailProtectionSignupPromptResponse",
    val success: SignupResponse,
) {
    data class SignupResponse(
        val isSignedIn: Boolean,
    )
}
