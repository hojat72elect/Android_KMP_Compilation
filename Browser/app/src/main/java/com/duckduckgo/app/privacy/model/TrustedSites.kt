

package com.duckduckgo.app.privacy.model

import android.net.Uri
import com.duckduckgo.app.browser.UriString
import com.duckduckgo.common.utils.AppUrl

class TrustedSites {

    companion object {

        private val trusted = listOf(
            AppUrl.Url.HOST,
            "duckduckgo.com",
            "donttrack.us",
            "spreadprivacy.com",
            "duckduckhack.com",
            "privatebrowsingmyths.com",
            "duck.co",
        )

        fun isTrusted(url: String): Boolean {
            return trusted.any { UriString.sameOrSubdomain(url, it) }
        }

        fun isTrusted(url: Uri): Boolean {
            return trusted.any { UriString.sameOrSubdomain(url, it) }
        }
    }
}
