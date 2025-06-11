

package com.duckduckgo.privacy.config.api

/** Public interface for the Https (Smart Encryption) feature */
interface Https {
    /**
     * This method takes a [url] and returns `true` or `false` depending if the [url] is in the
     * https exceptions list
     * @return a `true` if the given [url] if the url is in the https exceptions list and `false`
     * otherwise.
     */
    fun isAnException(url: String): Boolean
}
