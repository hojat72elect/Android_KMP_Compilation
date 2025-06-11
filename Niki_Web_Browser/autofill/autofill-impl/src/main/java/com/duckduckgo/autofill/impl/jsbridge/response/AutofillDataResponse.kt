

package com.duckduckgo.autofill.impl.jsbridge.response

data class AutofillDataResponse(
    val type: String = AUTOFILL_DATA_RESPONSE,
    val success: CredentialSuccessResponse,
) {

    data class CredentialSuccessResponse(
        val username: String? = "",
        val password: String? = null,
    )
}

data class AutofillAvailableInputTypesResponse(
    val type: String = AVAILABLE_INPUT_TYPES_RESPONSE,
    val success: AvailableInputSuccessResponse,
) {

    data class AvailableInputSuccessResponse(
        val credentials: Boolean,
    )
}

private const val AUTOFILL_DATA_RESPONSE = "getAutofillDataResponse"
private const val AVAILABLE_INPUT_TYPES_RESPONSE = "getAvailableInputTypesResponse"
