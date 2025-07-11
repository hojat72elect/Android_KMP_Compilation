

package com.duckduckgo.subscriptions.impl.rmf

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.JsonMatchingAttribute
import com.duckduckgo.remote.messaging.api.JsonToMatchingAttributeMapper
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.subscriptions.api.Subscriptions
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
class RMFPProSubscriberMatchingAttribute @Inject constructor(
    private val subscriptions: Subscriptions,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is ProSubscriberMatchingAttribute -> subscriptions.isSignedIn() == matchingAttribute.remoteValue
            else -> null
        }
    }

    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? {
        return when (key) {
            ProSubscriberMatchingAttribute.KEY -> {
                jsonMatchingAttribute.value?.let {
                    ProSubscriberMatchingAttribute(jsonMatchingAttribute.value as Boolean)
                }
            }
            else -> null
        }
    }
}

private data class ProSubscriberMatchingAttribute(
    val remoteValue: Boolean,
) : MatchingAttribute {
    companion object {
        const val KEY = "pproSubscriber"
    }
}
