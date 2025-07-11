

package com.duckduckgo.subscriptions.impl.rmf

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.JsonMatchingAttribute
import com.duckduckgo.remote.messaging.api.JsonToMatchingAttributeMapper
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.subscriptions.impl.SubscriptionsManager
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = JsonToMatchingAttributeMapper::class,
)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = AttributeMatcherPlugin::class,
)
@SingleInstanceIn(AppScope::class)
class RMFPProPurchasePlatformMatchingAttribute @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {

    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is PProPurchasePlatformMatchingAttribute -> {
                assert(matchingAttribute.supportedPlatforms.isNotEmpty())
                val purchasePlatform = subscriptionsManager.getSubscription()?.platform
                return !purchasePlatform.isNullOrEmpty() && matchingAttribute.supportedPlatforms.contains(purchasePlatform, ignoreCase = true)
            }

            else -> null
        }
    }

    private fun List<String>.contains(
        s: String,
        ignoreCase: Boolean = false,
    ): Boolean {
        return any { it.equals(s, ignoreCase) }
    }

    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? {
        return when (key) {
            PProPurchasePlatformMatchingAttribute.KEY -> {
                val value = jsonMatchingAttribute.value as? List<String>
                value.takeUnless { it.isNullOrEmpty() }?.let { platforms ->
                    PProPurchasePlatformMatchingAttribute(platforms)
                }
            }

            else -> null
        }
    }
}

internal data class PProPurchasePlatformMatchingAttribute(
    val supportedPlatforms: List<String>,
) : MatchingAttribute {
    companion object {
        const val KEY = "pproPurchasePlatform"
    }
}
