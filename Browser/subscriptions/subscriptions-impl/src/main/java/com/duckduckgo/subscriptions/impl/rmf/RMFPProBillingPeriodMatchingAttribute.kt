

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
class RMFPProBillingPeriodMatchingAttribute @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        if (matchingAttribute is PProBillingPeriodMatchingAttribute) {
            return matchingAttribute.value == subscriptionsManager.getSubscription()?.billingPeriod
        }
        return null
    }

    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? {
        if (key == PProBillingPeriodMatchingAttribute.KEY) {
            val value = jsonMatchingAttribute.value as? String
            return value.takeIf { !it.isNullOrEmpty() }?.let {
                PProBillingPeriodMatchingAttribute(value = it)
            }
        }
        return null
    }
}

internal data class PProBillingPeriodMatchingAttribute(
    val value: String,
) : MatchingAttribute {
    companion object {
        const val KEY = "pproBillingPeriod"
    }
}
