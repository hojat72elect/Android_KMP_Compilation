

package com.duckduckgo.app.browser

import android.content.Context
import android.webkit.WebViewDatabase

interface WebViewDatabaseProvider {
    fun get(): WebViewDatabase
}

class DefaultWebViewDatabaseProvider(val context: Context) : WebViewDatabaseProvider {

    @Volatile
    private var instance: WebViewDatabase? = null

    override fun get(): WebViewDatabase =
        instance ?: synchronized(this) {
            instance ?: WebViewDatabase.getInstance(context).also { instance = it }
        }
}
