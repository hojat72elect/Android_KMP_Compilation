

package com.duckduckgo.remote.messaging.api

interface MessageActionMapperPlugin {
    fun evaluate(jsonMessageAction: JsonMessageAction): Action?
}

data class JsonMessageAction(
    val type: String,
    val value: String,
    val additionalParameters: Map<String, String>?,
)

sealed class JsonActionType(val jsonValue: String) {
    object URL : JsonActionType("url")
    object PLAYSTORE : JsonActionType("playstore")
    object DEFAULT_BROWSER : JsonActionType("defaultBrowser")
    object DISMISS : JsonActionType("dismiss")
    object APP_TP_ONBOARDING : JsonActionType("atpOnboarding")
    object SHARE : JsonActionType("share")
    object NAVIGATION : JsonActionType("navigation")
    object SURVEY : JsonActionType("survey")
}
