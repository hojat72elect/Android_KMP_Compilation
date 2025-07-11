

package com.duckduckgo.app.browser

import android.content.Context
import androidx.webkit.WebViewCompat
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface WebViewVersionSource {
    fun get(): String
}

@ContributesBinding(AppScope::class)
class WebViewCompatWebViewVersionSource @Inject constructor(
    private val context: Context,
) : WebViewVersionSource {
    override fun get(): String =
        WebViewCompat.getCurrentWebViewPackage(context)?.versionName ?: ""
}
