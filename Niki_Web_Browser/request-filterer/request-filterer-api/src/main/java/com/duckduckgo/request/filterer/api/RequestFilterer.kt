

package com.duckduckgo.request.filterer.api

import android.webkit.WebResourceRequest

/** Public interface for the Request Filterer feature */
interface RequestFilterer {
    /**
     * This method takes a [request] and a [documentUrl] to calculate if the request should be filtered out or not.
     * @return `true` if the request should be filtered or `false` if it shouldn't
     */
    fun shouldFilterOutRequest(request: WebResourceRequest, documentUrl: String?): Boolean

    /**
     * This method takes a [url] and registers it internally. This method must be used before using `shouldFilterOutRequest`
     * to correctly register the different page created events.
     */
    fun registerOnPageCreated(url: String)
}
