

package com.duckduckgo.pir.internal.scripts.models

data class PirScriptRequestParams(
    val state: ActionRequest,
)

data class ActionRequest(
    val action: BrokerAction,
    val data: PirScriptRequestData? = null,
)

sealed class PirScriptRequestData {
    data class SolveCaptcha(
        val token: String,
    ) : PirScriptRequestData()

    data class UserProfile(
        val userProfile: ProfileQuery? = null,
        val extractedProfile: ExtractedProfileParams? = null,
    ) : PirScriptRequestData()
}

data class ExtractedProfileParams(
    val id: Int? = null,
    val name: String? = null,
    val profileUrl: String? = null,
    val email: String? = null,
    val fullName: String? = null,
)
