

package com.duckduckgo.app.browser.urlextraction

interface UrlExtractionListener {
    fun onUrlExtractionError(initialUrl: String)
    fun onUrlExtracted(initialUrl: String, extractedUrl: String?)
}
