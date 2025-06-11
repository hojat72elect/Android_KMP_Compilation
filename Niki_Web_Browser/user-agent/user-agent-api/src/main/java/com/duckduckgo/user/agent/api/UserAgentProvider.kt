

package com.duckduckgo.user.agent.api

interface UserAgentProvider {
    /**
     * Provides the user agent based on a specific URL and if the website should be displayed in desktop mode or not
     * @param url where the ua will be set
     * @param isDesktop boolean to know if the website should be displayed in desktop mode or not
     * @return a string with the user agent
     */
    fun userAgent(url: String? = null, isDesktop: Boolean = false): String
}

/**
 * A plugin to intercept the user agent, useful when we want to re-write the user agent for specific cases. Only used internally so far.
 */
interface UserAgentInterceptor {
    /**
     * @param userAgent the user agent initially set
     * @return a string with the new user agent
     */
    fun intercept(userAgent: String): String
}
