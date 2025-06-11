

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
class ShownMessageMatcher @Inject constructor(
    private val remoteMessagingRepository: RemoteMessagingRepository,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {
    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? {
        return when (key) {
            "messageShown" -> {
                val value = jsonMatchingAttribute.value
                if (value is List<*>) {
                    val messageIds = value.filterIsInstance<String>()
                    if (messageIds.isNotEmpty()) {
                        return ShownMessageMatchingAttribute(messageIds)
                    }
                }

                return null
            }

            else -> null
        }
    }

    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        if (matchingAttribute is ShownMessageMatchingAttribute) {
            assert(matchingAttribute.messageIds.isNotEmpty())

            val currentMessage = remoteMessagingRepository.message()
            matchingAttribute.messageIds.forEach {
                if (currentMessage?.id != it) {
                    if (remoteMessagingRepository.didShow(it)) return true
                }
            }
            return false
        }
        return null
    }
}

data class ShownMessageMatchingAttribute(
    val messageIds: List<String>,
) : MatchingAttribute
