

package com.duckduckgo.app.browser.omnibar

import android.net.Uri
import android.webkit.URLUtil
import com.duckduckgo.app.browser.RequestRewriter
import com.duckduckgo.app.browser.UriString
import com.duckduckgo.common.utils.AppUrl
import com.duckduckgo.common.utils.AppUrl.Url
import com.duckduckgo.common.utils.UrlScheme.Companion.https
import com.duckduckgo.common.utils.withScheme
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class QueryUrlConverter @Inject constructor(private val requestRewriter: RequestRewriter) : OmnibarEntryConverter {

    override fun convertQueryToUrl(
        searchQuery: String,
        vertical: String?,
        queryOrigin: QueryOrigin,
    ): String {
        val isUrl = when (queryOrigin) {
            is QueryOrigin.FromAutocomplete -> queryOrigin.isNav
            is QueryOrigin.FromUser -> UriString.isWebUrl(searchQuery) || UriString.isDuckUri(searchQuery)
        }

        if (isUrl == true) {
            return convertUri(searchQuery)
        }

        if (URLUtil.isDataUrl(searchQuery) || URLUtil.isAssetUrl(searchQuery)) {
            return searchQuery
        }

        val uriBuilder = Uri.Builder()
            .scheme(https)
            .appendQueryParameter(AppUrl.ParamKey.QUERY, searchQuery)
            .authority(Url.HOST)

        if (vertical != null && majorVerticals.contains(vertical)) {
            uriBuilder.appendQueryParameter(AppUrl.ParamKey.VERTICAL_REWRITE, vertical)
        }

        requestRewriter.addCustomQueryParams(uriBuilder)
        return uriBuilder.build().toString()
    }

    private fun convertUri(input: String): String {
        val uri = Uri.parse(input).withScheme()

        if (requestRewriter.shouldRewriteRequest(uri)) {
            return requestRewriter.rewriteRequestWithCustomQueryParams(uri).toString()
        }

        return uri.toString()
    }

    companion object {
        val majorVerticals = listOf("images", "videos", "news", "shopping")
    }
}
