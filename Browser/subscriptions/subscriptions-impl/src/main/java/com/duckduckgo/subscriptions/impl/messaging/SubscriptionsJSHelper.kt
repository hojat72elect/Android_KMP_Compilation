

package com.duckduckgo.subscriptions.impl.messaging

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.js.messaging.api.JsCallbackData
import com.duckduckgo.subscriptions.api.SubscriptionsJSHelper
import com.duckduckgo.subscriptions.impl.SubscriptionsManager
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import org.json.JSONArray
import org.json.JSONObject

@ContributesBinding(AppScope::class)
class RealSubscriptionsJSHelper @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
) : SubscriptionsJSHelper {

    override suspend fun processJsCallbackMessage(
        featureName: String,
        method: String,
        id: String?,
        data: JSONObject?,
    ): JsCallbackData? = when (method) {
        METHOD_HANDSHAKE -> id?.let {
            val jsonPayload = JSONObject().apply {
                put(AVAILABLE_MESSAGES, JSONArray().put(SUBSCRIPTION_DETAILS))
                put(PLATFORM, ANDROID)
            }
            return JsCallbackData(jsonPayload, featureName, method, id)
        }

        METHOD_SUBSCRIPTION_DETAILS -> id?.let {
            getSubscriptionDetailsData(featureName, method, it)
        }

        else -> null
    }

    private suspend fun getSubscriptionDetailsData(featureName: String, method: String, id: String): JsCallbackData {
        val jsonPayload = subscriptionsManager.getSubscription()?.let { userSubscription ->
            JSONObject().apply {
                put(IS_SUBSCRIBED, userSubscription.isActive())
                put(BILLING_PERIOD, userSubscription.billingPeriod)
                put(STARTED_AT, userSubscription.startedAt)
                put(EXPIRES_OR_RENEWS_AT, userSubscription.expiresOrRenewsAt)
                put(PAYMENT_PLATFORM, userSubscription.platform)
                put(STATUS, userSubscription.status.statusName)
            }
        } ?: JSONObject().apply {
            put(IS_SUBSCRIBED, false)
        }

        return JsCallbackData(jsonPayload, featureName, method, id)
    }

    companion object {
        private const val METHOD_HANDSHAKE = "handshake"
        private const val METHOD_SUBSCRIPTION_DETAILS = "subscriptionDetails"
        private const val AVAILABLE_MESSAGES = "availableMessages"
        private const val SUBSCRIPTION_DETAILS = "subscriptionDetails"
        private const val PLATFORM = "platform"
        private const val ANDROID = "android"
        private const val IS_SUBSCRIBED = "isSubscribed"
        private const val BILLING_PERIOD = "billingPeriod"
        private const val STARTED_AT = "startedAt"
        private const val EXPIRES_OR_RENEWS_AT = "expiresOrRenewsAt"
        private const val PAYMENT_PLATFORM = "paymentPlatform"
        private const val STATUS = "status"
    }
}
