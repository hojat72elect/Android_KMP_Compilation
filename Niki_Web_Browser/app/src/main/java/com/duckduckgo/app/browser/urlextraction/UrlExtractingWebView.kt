package com.duckduckgo.app.browser.urlextraction

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebSettings
import android.webkit.WebView
import com.duckduckgo.app.browser.BuildConfig
import com.duckduckgo.user.agent.api.UserAgentProvider

@SuppressLint("SetJavaScriptEnabled", "ViewConstructor")
class UrlExtractingWebView(
    context: Context,
    webViewClient: UrlExtractingWebViewClient,
    userAgentProvider: UserAgentProvider,
    urlExtractor: DOMUrlExtractor,
) : WebView(context) {

    var urlExtractionListener: UrlExtractionListener? = null
    lateinit var initialUrl: String

    init {
        settings.apply {
            userAgentString = userAgentProvider.userAgent()
            javaScriptEnabled = true
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            disableWebSql(this)
            loadsImagesAutomatically = false
        }
        setWebViewClient(webViewClient)

        if (BuildConfig.DEBUG) {
            setWebContentsDebuggingEnabled(true)
        }

        urlExtractor.addUrlExtraction(this) { extractedUrl ->
            urlExtractionListener?.onUrlExtracted(initialUrl, extractedUrl)
        }
    }

    /**
     * Explicitly disable database to try protect against Magellan WebSQL/SQLite vulnerability
     */
    private fun disableWebSql(settings: WebSettings) {
        settings.databaseEnabled = false
    }

    override fun loadUrl(url: String) {
        initialUrl = url
        super.loadUrl(url)
    }

    fun destroyWebView() {
        stopLoading()
        clearHistory()
        loadUrl("about:blank")
        onPause()
        removeAllViews()
        destroy()
    }
}
