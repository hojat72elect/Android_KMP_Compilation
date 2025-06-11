

package com.duckduckgo.malicioussiteprotection.impl.models

import com.duckduckgo.malicioussiteprotection.api.MaliciousSiteProtection.Feed
import com.duckduckgo.malicioussiteprotection.api.MaliciousSiteProtection.Feed.MALWARE
import com.duckduckgo.malicioussiteprotection.api.MaliciousSiteProtection.Feed.PHISHING
import com.duckduckgo.malicioussiteprotection.api.MaliciousSiteProtection.Feed.SCAM

data class Match(
    val hostname: String,
    val url: String,
    val regex: String,
    val hash: String,
    val feed: Feed,
)

data class Filter(
    val hash: String,
    val regex: String,
)

class FilterSet(
    val filters: Filter,
    val feed: Feed,
)

sealed class FilterSetWithRevision(
    open val insert: Set<Filter>,
    open val delete: Set<Filter>,
    open val revision: Int,
    open val replace: Boolean,
    val feed: Feed,
    val type: Type = Type.FILTER_SET,
) {
    data class PhishingFilterSetWithRevision(
        override val insert: Set<Filter>,
        override val delete: Set<Filter>,
        override val revision: Int,
        override val replace: Boolean,
    ) : FilterSetWithRevision(insert, delete, revision, replace, PHISHING)

    data class MalwareFilterSetWithRevision(
        override val insert: Set<Filter>,
        override val delete: Set<Filter>,
        override val revision: Int,
        override val replace: Boolean,
    ) : FilterSetWithRevision(insert, delete, revision, replace, MALWARE)

    data class ScamFilterSetWithRevision(
        override val insert: Set<Filter>,
        override val delete: Set<Filter>,
        override val revision: Int,
        override val replace: Boolean,
    ) : FilterSetWithRevision(insert, delete, revision, replace, SCAM)
}

sealed class HashPrefixesWithRevision(
    open val insert: Set<String>,
    open val delete: Set<String>,
    open val revision: Int,
    open val replace: Boolean,
    val feed: Feed,
    val type: Type = Type.HASH_PREFIXES,
) {
    data class PhishingHashPrefixesWithRevision(
        override val insert: Set<String>,
        override val delete: Set<String>,
        override val revision: Int,
        override val replace: Boolean,
    ) : HashPrefixesWithRevision(insert, delete, revision, replace, PHISHING)

    data class MalwareHashPrefixesWithRevision(
        override val insert: Set<String>,
        override val delete: Set<String>,
        override val revision: Int,
        override val replace: Boolean,
    ) : HashPrefixesWithRevision(insert, delete, revision, replace, MALWARE)

    data class ScamHashPrefixesWithRevision(
        override val insert: Set<String>,
        override val delete: Set<String>,
        override val revision: Int,
        override val replace: Boolean,
    ) : HashPrefixesWithRevision(insert, delete, revision, replace, SCAM)
}

enum class Type {
    HASH_PREFIXES,
    FILTER_SET,
}

sealed class MatchesResult {
    data class Result(val matches: List<Match>) : MatchesResult()
    data object Ignored : MatchesResult()
}
