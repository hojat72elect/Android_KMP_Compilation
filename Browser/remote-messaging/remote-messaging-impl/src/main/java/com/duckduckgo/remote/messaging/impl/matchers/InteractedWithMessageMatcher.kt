

package com.duckduckgo.remote.messaging.impl.matchers

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.JsonMatchingAttribute
import com.duckduckgo.remote.messaging.api.JsonToMatchingAttributeMapper
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.remote.messaging.api.RemoteMessagingRepository
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
class InteractedWithMessageMatcher @Inject constructor(
    private val remoteMessagingRepository: RemoteMessagingRepository,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {
    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? {
        return when (key) {
            "interactedWithMessage" -> {
                val value = jsonMatchingAttribute.value as? List<String>
                value.takeUnless { it.isNullOrEmpty() }?.let { messageIds ->
                    InteractedWithMessageMatchingAttribute(messageIds)
                }
            }

            else -> null
        }
    }

    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        if (matchingAttribute is InteractedWithMessageMatchingAttribute) {
            assert(matchingAttribute.messageIds.isNotEmpty())

            remoteMessagingRepository.dismissedMessages().forEach {
                if ((matchingAttribute.messageIds).contains(it)) {
                    return true
                }
            }
            return false
        }
        return null
    }
}

data class InteractedWithMessageMatchingAttribute(
    val messageIds: List<String>,
) : MatchingAttribute
