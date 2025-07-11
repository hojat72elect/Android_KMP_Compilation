

package com.duckduckgo.cookies.api

import android.webkit.CookieManager

/** Public interface for CookieManagerProvider */
interface CookieManagerProvider {
    /**
     * This method returns the [CookieManager] instance. This is a singleton instance.
     */
    fun get(): CookieManager?
}
