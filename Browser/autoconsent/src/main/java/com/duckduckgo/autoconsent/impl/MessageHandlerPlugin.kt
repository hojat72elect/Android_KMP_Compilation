package com.duckduckgo.autoconsent.impl

import android.webkit.WebView
import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.autoconsent.api.AutoconsentCallback
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = MessageHandlerPlugin::class,
)
@Suppress("unused")
interface MessageHandlerPlugin {
    fun process(messageType: String, jsonString: String, webView: WebView, autoconsentCallback: AutoconsentCallback)
    val supportedTypes: List<String>
}
