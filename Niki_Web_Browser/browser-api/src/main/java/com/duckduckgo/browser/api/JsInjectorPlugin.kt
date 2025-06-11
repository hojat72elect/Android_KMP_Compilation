

package com.duckduckgo.browser.api

import android.webkit.WebView
import com.duckduckgo.app.global.model.Site

/** Public interface to inject JS code to a website */
interface JsInjectorPlugin {
    /**
     * This method is called during onPageStarted and receives a [webView] instance, the [url] of the website and the [site]
     */
    fun onPageStarted(webView: WebView, url: String?, site: Site?)

    /**
     * This method is called during onPageFinished and receives a [webView] instance, the [url] of the website and the [site]
     */
    fun onPageFinished(webView: WebView, url: String?, site: Site?)
}
