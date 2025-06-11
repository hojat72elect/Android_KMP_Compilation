

package com.duckduckgo.autofill.impl.jsbridge.request

import com.duckduckgo.autofill.impl.jsbridge.request.SupportedAutofillInputSubType.PASSWORD
import com.duckduckgo.autofill.impl.jsbridge.request.SupportedAutofillInputSubType.USERNAME
import com.squareup.moshi.Json

data class AutofillDataRequest(
    val mainType: SupportedAutofillInputMainType,
    val subType: SupportedAutofillInputSubType,
    val trigger: SupportedAutofillTriggerType,
    val generatedPassword: GeneratedPasswordRequest?,
) {

    fun isGeneratedPasswordAvailable(): Boolean {
        return mainType == SupportedAutofillInputMainType.CREDENTIALS &&
            subType == PASSWORD &&
            generatedPassword?.value != null
    }

    fun isAutofillCredentialsRequest(): Boolean {
        return mainType == SupportedAutofillInputMainType.CREDENTIALS &&
            (subType == USERNAME || subType == PASSWORD)
    }
}

data class GeneratedPasswordRequest(
    val value: String?,
    val username: String?,
)

enum class SupportedAutofillInputType {
    @Json(name = "credentials.password")
    AUTOFILL_PASSWORD,

    @Json(name = "credentials.username")
    AUTOFILL_CREDENTIALS,
}

enum class SupportedAutofillInputMainType {
    @Json(name = "credentials")
    CREDENTIALS,

    @Json(name = "identities")
    IDENTITIES,

    @Json(name = "creditCards")
    CREDIT_CARDS,
}

enum class SupportedAutofillInputSubType {
    @Json(name = "username")
    USERNAME,

    @Json(name = "password")
    PASSWORD,
}

enum class SupportedAutofillTriggerType {
    @Json(name = "userInitiated")
    USER_INITIATED,

    @Json(name = "autoprompt")
    AUTOPROMPT,
}

enum class FormSubmissionTriggerType {
    @Json(name = "formSubmission")
    FORM_SUBMISSION,

    @Json(name = "partialSave")
    PARTIAL_SAVE,

    UNKNOWN,

    ;
}
