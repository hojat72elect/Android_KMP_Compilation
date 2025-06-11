

package com.duckduckgo.app.browser

import android.net.Uri
import com.duckduckgo.common.utils.AppUrl
import com.duckduckgo.common.utils.AppUrl.ParamKey
import com.duckduckgo.common.utils.AppUrl.ParamValue
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import okhttp3.HttpUrl.Companion.toHttpUrl

@ContributesBinding(AppScope::class)
class DuckDuckGoUrlDetectorImpl @Inject constructor() : DuckDuckGoUrlDetector {

    override fun isDuckDuckGoEmailUrl(url: String): Boolean {
        val uri = url.toUri()
        val firstSegment = uri.pathSegments.firstOrNull()
        return isDuckDuckGoUrl(url) && firstSegment == AppUrl.Url.EMAIL_SEGMENT
    }

    override fun isDuckDuckGoUrl(url: String): Boolean {
        return runCatching { AppUrl.Url.HOST == url.toHttpUrl().topPrivateDomain() }.getOrElse { false }
    }

    override fun isDuckDuckGoQueryUrl(uri: String): Boolean {
        return isDuckDuckGoUrl(uri) && hasQuery(uri)
    }

    override fun isDuckDuckGoStaticUrl(uri: String): Boolean {
        return isDuckDuckGoUrl(uri) && matchesStaticPage(uri)
    }

    private fun matchesStaticPage(uri: String): Boolean {
        return when (uri.toUri().path) {
            AppUrl.StaticUrl.SETTINGS -> true
            AppUrl.StaticUrl.PARAMS -> true
            else -> false
        }
    }

    private fun hasQuery(uri: String): Boolean {
        return uri.toUri().queryParameterNames.contains(ParamKey.QUERY)
    }

    override fun extractQuery(uriString: String): String? {
        val uri = uriString.toUri()
        return uri.getQueryParameter(ParamKey.QUERY)
    }

    override fun isDuckDuckGoVerticalUrl(uri: String): Boolean {
        return isDuckDuckGoUrl(uri) && hasVertical(uri)
    }

    private fun hasVertical(uri: String): Boolean {
        return uri.toUri().queryParameterNames.contains(ParamKey.VERTICAL)
    }

    override fun extractVertical(uriString: String): String? {
        val uri = uriString.toUri()
        return uri.getQueryParameter(ParamKey.VERTICAL)
    }

    override fun isDuckDuckGoChatUrl(uri: String): Boolean {
        return isDuckDuckGoUrl(uri) && hasAIChatVertical(uri)
    }

    private fun hasAIChatVertical(uri: String): Boolean {
        val vertical = extractVertical(uri)
        return vertical == ParamValue.CHAT_VERTICAL
    }

    private fun String.toUri(): Uri {
        return Uri.parse(this)
    }
}
