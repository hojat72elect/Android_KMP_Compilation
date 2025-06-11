

package com.duckduckgo.cookies.api

/**
 * Checks if the given cookie string contains any excluded third party cookie names.
 *
 * @param cookieString The string representation of cookies to check.
 * @return `true` if the `cookieString` contains any of the excluded cookie names, `false` otherwise.
 */
interface ThirdPartyCookieNames {
    fun hasExcludedCookieName(cookieString: String): Boolean
}
