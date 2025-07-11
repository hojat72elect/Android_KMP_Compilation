

package com.duckduckgo.voice.impl.rmf

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.JsonMatchingAttribute
import com.duckduckgo.remote.messaging.api.JsonToMatchingAttributeMapper
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.voice.api.VoiceSearchAvailability
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

data class VoiceSearchEnabledMatchingAttribute(
    val remoteValue: Boolean,
) : MatchingAttribute {
    companion object {
        const val KEY = "voiceSearchEnabled"
    }
}

data class VoiceSearchSupportedMatchingAttribute(
    val remoteValue: Boolean,
) : MatchingAttribute {
    companion object {
        const val KEY = "voiceSearchSupported"
    }
}

@ContributesMultibinding(AppScope::class)
class VoiceSearchAttributeMatcherPlugin @Inject constructor(
    private val voiceSearchAvailability: VoiceSearchAvailability,
) : AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is VoiceSearchSupportedMatchingAttribute -> {
                matchingAttribute.remoteValue == voiceSearchAvailability.isVoiceSearchSupported
            }

            is VoiceSearchEnabledMatchingAttribute -> {
                matchingAttribute.remoteValue == voiceSearchAvailability.isVoiceSearchAvailable
            }

            else -> return null
        }
    }
}

@ContributesMultibinding(AppScope::class)
class VoiceSearchJsonMatchingAttributeMapper @Inject constructor() : JsonToMatchingAttributeMapper {
    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? {
        return when (key) {
            VoiceSearchEnabledMatchingAttribute.KEY -> {
                VoiceSearchEnabledMatchingAttribute(
                    jsonMatchingAttribute.value as Boolean,
                )
            }
            VoiceSearchSupportedMatchingAttribute.KEY -> {
                VoiceSearchSupportedMatchingAttribute(
                    jsonMatchingAttribute.value as Boolean,
                )
            }
            else -> null
        }
    }
}
