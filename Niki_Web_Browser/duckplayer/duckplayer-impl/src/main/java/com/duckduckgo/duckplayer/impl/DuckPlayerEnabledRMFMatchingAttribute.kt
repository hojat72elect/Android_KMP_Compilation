

package com.duckduckgo.duckplayer.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.duckplayer.api.DuckPlayer
import com.duckduckgo.duckplayer.api.PrivatePlayerMode.AlwaysAsk
import com.duckduckgo.duckplayer.api.PrivatePlayerMode.Enabled
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.JsonMatchingAttribute
import com.duckduckgo.remote.messaging.api.JsonToMatchingAttributeMapper
import com.duckduckgo.remote.messaging.api.MatchingAttribute
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
class DuckPlayerEnabledRMFMatchingAttribute @Inject constructor(
    private val duckPlayer: DuckPlayer,
    private val duckPlayerFeature: DuckPlayerFeature,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        val duckPlayerEnabled = duckPlayerFeature.self().isEnabled() && duckPlayerFeature.enableDuckPlayer().isEnabled() &&
            duckPlayer.getUserPreferences().privatePlayerMode.let { it == AlwaysAsk || it == Enabled }
        return when (matchingAttribute) {
            is DuckPlayerEnabledMatchingAttribute -> duckPlayerEnabled == matchingAttribute.remoteValue
            else -> null
        }
    }

    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? {
        return when (key) {
            DuckPlayerEnabledMatchingAttribute.KEY -> {
                jsonMatchingAttribute.value?.let {
                    DuckPlayerEnabledMatchingAttribute(jsonMatchingAttribute.value as Boolean)
                }
            }
            else -> null
        }
    }
}

internal data class DuckPlayerEnabledMatchingAttribute(
    val remoteValue: Boolean,
) : MatchingAttribute {
    companion object {
        const val KEY = "duckPlayerEnabled"
    }
}
