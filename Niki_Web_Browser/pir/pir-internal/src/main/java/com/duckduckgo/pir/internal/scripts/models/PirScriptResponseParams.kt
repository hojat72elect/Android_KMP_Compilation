

package com.duckduckgo.pir.internal.scripts.models

data class PirResult(
    val result: PirScriptResultResponse?,
)

data class PirScriptResultResponse(
    val success: PirSuccessResponse? = null,
    val error: PirErrorReponse? = null,
)

data class PirErrorReponse(
    val actionID: String,
    val message: String,
)

sealed class PirSuccessResponse(
    open val actionID: String,
    open val actionType: String,
) {
    data class NavigateResponse(
        override val actionID: String,
        override val actionType: String,
        val response: ResponseData,
    ) : PirSuccessResponse(actionID, actionType) {
        data class ResponseData(
            val url: String,
        )
    }

    data class ExtractedResponse(
        override val actionID: String,
        override val actionType: String,
        val meta: AdditionalData? = null,
    ) : PirSuccessResponse(actionID, actionType) {
        data class AdditionalData(
            val userData: ProfileQuery,
            val extractResults: List<ScrapedData>,
        )
        data class ScrapedData(
            val scrapedData: ExtractedProfile,
            val result: Boolean,
            val score: Int,
            val matchedFields: List<String>,
        )
    }

    data class GetCaptchaInfoResponse(
        override val actionID: String,
        override val actionType: String,
        val meta: AdditionalData? = null,
        val response: ResponseData? = null,
    ) : PirSuccessResponse(actionID, actionType) {
        data class ResponseData(
            val siteKey: String,
            val url: String,
            val type: String,
        )
    }

    data class SolveCaptchaResponse(
        override val actionID: String,
        override val actionType: String,
        val meta: AdditionalData? = null,
        val response: ResponseData? = null,
    ) : PirSuccessResponse(actionID, actionType) {
        data class ResponseData(
            val callback: CallbackData,
        )
        data class CallbackData(
            val eval: String,
        )
    }

    data class FillFormResponse(
        override val actionID: String,
        override val actionType: String,
        val meta: AdditionalData? = null,
    ) : PirSuccessResponse(actionID, actionType)

    data class ClickResponse(
        override val actionID: String,
        override val actionType: String,
        val meta: AdditionalData? = null,
    ) : PirSuccessResponse(actionID, actionType)

    data class ExpectationResponse(
        override val actionID: String,
        override val actionType: String,
        val meta: AdditionalData? = null,
    ) : PirSuccessResponse(actionID, actionType)

    data class AdditionalData(
        val additionalData: String,
    )
}
