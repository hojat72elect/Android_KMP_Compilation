

package com.duckduckgo.privacy.config.api

/** Public interface for the Content Blocking feature */
interface ContentBlocking {
    /**
     * This method takes a [url] and returns `true` or `false` depending if the [url] is in the
     * content blocking exceptions list
     * @return a `true` if the given [url] if the url is in the content blocking exceptions list and
     * `false` otherwise.
     */
    fun isAnException(url: String): Boolean
}
