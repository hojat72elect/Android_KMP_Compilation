

package com.duckduckgo.downloads.impl

import android.webkit.CookieManager
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

// This class is basically a convenience wrapper for easier testing
interface CookieManagerWrapper {
    /**
     * @return the cookie stored for the given [url] if any, null otherwise
     */
    fun getCookie(url: String): String?
}

@ContributesBinding(AppScope::class)
class CookieManagerWrapperImpl @Inject constructor() : CookieManagerWrapper {
    override fun getCookie(url: String): String? {
        return CookieManager.getInstance().getCookie(url)
    }
}
