

package com.duckduckgo.remote.messaging.impl.matchers

sealed class EvaluationResult {
    object Match : EvaluationResult()
    object Fail : EvaluationResult()
    object NextMessage : EvaluationResult()

    companion object {
        fun fromBoolean(result: Boolean): EvaluationResult {
            return if (result) Match else Fail
        }
    }
}

internal fun Boolean?.toResult(): EvaluationResult {
    return when (this) {
        true -> EvaluationResult.Match
        false -> EvaluationResult.Fail
        null -> EvaluationResult.NextMessage
    }
}
