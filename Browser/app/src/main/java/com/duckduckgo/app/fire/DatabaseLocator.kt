

package com.duckduckgo.app.fire

import android.content.Context

class WebViewDatabaseLocator(context: Context) : DatabaseLocator(context) {
    override val knownLocations = listOf("/app_webview/Default/Cookies", "/app_webview/Cookies")
}

class AuthDatabaseLocator(context: Context) : DatabaseLocator(context) {
    override val knownLocations = listOf("/databases/http_auth.db")
}
