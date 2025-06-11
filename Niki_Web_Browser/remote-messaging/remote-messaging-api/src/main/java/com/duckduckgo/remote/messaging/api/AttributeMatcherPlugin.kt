

package com.duckduckgo.remote.messaging.api

interface AttributeMatcherPlugin {
    suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean?
}

interface MatchingAttribute

interface JsonToMatchingAttributeMapper {
    fun map(key: String, jsonMatchingAttribute: JsonMatchingAttribute): MatchingAttribute?
}

data class JsonMatchingAttribute(
    val value: Any? = null,
    val min: Any? = null,
    val max: Any? = null,
    val since: Any? = null,
    val fallback: Boolean? = null,
)
