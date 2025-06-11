

package com.duckduckgo.remote.messaging.impl.models

import com.duckduckgo.remote.messaging.api.JsonMatchingAttribute
import com.duckduckgo.remote.messaging.api.JsonMessageAction

data class JsonRemoteMessagingConfig(
    val version: Long,
    val messages: List<JsonRemoteMessage>,
    val rules: List<JsonMatchingRule>,
)

data class JsonRemoteMessage(
    val id: String,
    val content: JsonContent?,
    val exclusionRules: List<Int>,
    val matchingRules: List<Int>,
    val translations: Map<String, JsonContentTranslations>,
)

data class JsonContent(
    val messageType: String = "",
    val titleText: String = "",
    val descriptionText: String = "",
    val placeholder: String = "",
    val primaryActionText: String = "",
    val primaryAction: JsonMessageAction? = null,
    val secondaryActionText: String = "",
    val secondaryAction: JsonMessageAction? = null,
    val actionText: String = "",
    val action: JsonMessageAction? = null,
)

data class JsonContentTranslations(
    val messageType: String = "",
    val titleText: String = "",
    val descriptionText: String = "",
    val primaryActionText: String = "",
    val secondaryActionText: String = "",
    val actionText: String = "",
)

data class JsonMatchingRule(
    val id: Int,
    val targetPercentile: JsonTargetPercentile?,
    val attributes: Map<String, JsonMatchingAttribute>?,
)

data class JsonTargetPercentile(
    val before: Float?,
)

sealed class JsonMessageType(val jsonValue: String) {
    object SMALL : JsonMessageType("small")
    object MEDIUM : JsonMessageType("medium")
    object BIG_SINGLE_ACTION : JsonMessageType("big_single_action")
    object BIG_TWO_ACTION : JsonMessageType("big_two_action")
    object PROMO_SINGLE_ACTION : JsonMessageType("promo_single_action")
}
