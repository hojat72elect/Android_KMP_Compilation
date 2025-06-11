

package com.duckduckgo.sync.impl.rmf

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.JsonMatchingAttribute
import com.duckduckgo.remote.messaging.api.JsonToMatchingAttributeMapper
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class SyncJsonMatchingAttributeMapper @Inject constructor() : JsonToMatchingAttributeMapper {
    override fun map(key: String, jsonMatchingAttribute: JsonMatchingAttribute): MatchingAttribute? {
        return when (key) {
            "syncEnabled" -> {
                SyncEnabledMatchingAttribute(
                    jsonMatchingAttribute.value as Boolean,
                )
            }
            else -> null
        }
    }
}

data class SyncEnabledMatchingAttribute(
    val remoteValue: Boolean,
) : MatchingAttribute
