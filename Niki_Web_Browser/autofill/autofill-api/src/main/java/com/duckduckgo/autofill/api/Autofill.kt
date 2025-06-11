

package com.duckduckgo.autofill.api

/** Public interface for the Autofill feature */
interface Autofill {
    /**
     * This method takes a [url] and returns `true` or `false` depending on if the [url] is in the
     * autofill exceptions list
     * @return a `true` if the given [url] if the url is in the autofill exceptions list and `false`
     * otherwise.
     */
    fun isAnException(url: String): Boolean
}
