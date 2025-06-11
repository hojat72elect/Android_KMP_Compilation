

package com.duckduckgo.privacy.config.api

/** Public interface for the Tracker Allowlist feature */
interface TrackerAllowlist {
    /**
     * This method takes a [documentURL] and a [url] and returns `true` or `false` depending if the
     * [url] and [documentURL] match the rules in the allowlist.
     * @return `true` if the given [url] and [documentURL] match the rules in the allowlist
     */
    fun isAnException(
        documentURL: String,
        url: String,
    ): Boolean
}
