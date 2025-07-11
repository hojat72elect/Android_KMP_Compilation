

package com.duckduckgo.pir.internal.scripts.models

sealed class PirError {
    data object MalformedURL : PirError()
    data object NoActionFound : PirError()
    data class ActionFailed(
        val actionID: String,
        val message: String,
    ) : PirError()

    data object ParsingErrorObjectFailed : PirError()
    data object UnknownMethodName : PirError()
    data object UserScriptMessageBrokerNotSet : PirError()
    data class Unknown(
        val error: String,
    ) : PirError()

    data object UnrecoverableError : PirError()
    data object NoOptOutStep : PirError()
    data class CaptchaServiceError(
        val error: String,
    ) : PirError()

    data class EmailError(
        val error: String?,
    ) : PirError()

    data object Cancelled : PirError()
    data object SolvingCaptchaWithCallbackError : PirError()
    data object CantCalculatePreferredRunDate : PirError()
    data class HttpError(
        val code: Int,
    ) : PirError()

    data object DataNotInDatabase : PirError()
}
