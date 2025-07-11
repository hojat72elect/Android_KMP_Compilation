

package com.duckduckgo.app.browser

/** Public interface for DuckDuckGoUrlDetector */
interface DuckDuckGoUrlDetector {

    /**
     * This method takes a [url] and returns `true` or `false`.
     * @return `true` if the given [url] belongs to the DuckDuckGo email domain and `false`
     * otherwise.
     */
    fun isDuckDuckGoEmailUrl(url: String): Boolean

    /**
     * This method takes a [url] and returns `true` or `false`.
     * @return `true` if the given [url] belongs to the DuckDuckGo domain and `false`
     * otherwise.
     */
    fun isDuckDuckGoUrl(url: String): Boolean

    /**
     * This method takes a [uri] and returns `true` or `false`.
     * @return `true` if the given [uri] is a DuckDuckGo query and `false`
     * otherwise.
     */
    fun isDuckDuckGoQueryUrl(uri: String): Boolean

    /**
     * This method takes a [uri] and returns `true` or `false`.
     * @return `true` if the given [uri] is a DuckDuckGo static URL and `false`
     * otherwise.
     */
    fun isDuckDuckGoStaticUrl(uri: String): Boolean

    /**
     * This method takes a [uriString] and returns a [String?]
     * @return the query of a given uri and null if it cannot find it.
     */
    fun extractQuery(uriString: String): String?

    /**
     * This method takes a [uri] and returns `true` or `false`.
     * @return `true` if the given [uri] is a DuckDuckGo vertical URL and `false`
     * otherwise.
     */
    fun isDuckDuckGoVerticalUrl(uri: String): Boolean

    /**
     * This method takes a [uriString] and returns a [String?]
     * @return the vertical of a given uri and null if it cannot find it.
     */
    fun extractVertical(uriString: String): String?

    /**
     * This method takes a [uri] and returns `true` or `false`.
     * @return `true` if the given [uri] is a DuckDuckGo Chat and `false`
     * otherwise.
     */
    fun isDuckDuckGoChatUrl(uri: String): Boolean
}
