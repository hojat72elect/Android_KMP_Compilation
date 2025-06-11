

package com.duckduckgo.cookies.api

/** Public interface for RemoveCookiesStrategy */
interface RemoveCookiesStrategy {
    /**
     * This method deletes all the cookies for the specific cookies strategy
     */
    suspend fun removeCookies()
}
