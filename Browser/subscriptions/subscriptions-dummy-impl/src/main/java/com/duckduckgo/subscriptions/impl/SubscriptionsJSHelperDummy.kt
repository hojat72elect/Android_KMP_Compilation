
package com.duckduckgo.subscriptions.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.js.messaging.api.JsCallbackData
import com.duckduckgo.subscriptions.api.SubscriptionsJSHelper
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import org.json.JSONObject

@ContributesBinding(AppScope::class)
class SubscriptionsJSHelperDummy @Inject constructor() : SubscriptionsJSHelper {
    override suspend fun processJsCallbackMessage(
        featureName: String,
        method: String,
        id: String?,
        data: JSONObject?,
    ): JsCallbackData? {
        return null
    }
}
