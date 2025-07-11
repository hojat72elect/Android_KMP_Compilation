

package com.duckduckgo.mobile.android.app.tracking

import com.duckduckgo.di.scopes.AppScope
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
class AppTPRMFMatchingAttribute @Inject constructor(
    private val appTrackingProtection: AppTrackingProtection,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is AppTPMatchingAttribute -> appTrackingProtection.isOnboarded() == matchingAttribute.remoteValue
            else -> null
        }
    }

    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? {
        return when (key) {
            AppTPMatchingAttribute.KEY -> {
                jsonMatchingAttribute.value?.let {
                    AppTPMatchingAttribute(jsonMatchingAttribute.value as Boolean)
                }
            }
            else -> null
        }
    }
}

internal data class AppTPMatchingAttribute(
    val remoteValue: Boolean,
) : MatchingAttribute {
    companion object {
        const val KEY = "atpOnboarded"
    }
}
