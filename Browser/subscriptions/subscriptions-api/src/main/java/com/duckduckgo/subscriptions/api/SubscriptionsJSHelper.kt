

package com.duckduckgo.subscriptions.api

import com.duckduckgo.js.messaging.api.JsCallbackData
import org.json.JSONObject

interface SubscriptionsJSHelper {
    suspend fun processJsCallbackMessage(
        featureName: String,
        method: String,
        id: String?,
        data: JSONObject?,
    ): JsCallbackData?
}

const val SUBSCRIPTIONS_FEATURE_NAME = "subscriptions"
