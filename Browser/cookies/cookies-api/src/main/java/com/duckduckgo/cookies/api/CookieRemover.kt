

package com.duckduckgo.cookies.api

/** Public interface for CookieRemover */
interface CookieRemover {
    /**
     * This method deletes all the cookies by the given [CookieRemover] and returns [true] if successful or [false] in case of a failure.
     */
    suspend fun removeCookies(): Boolean
}
