package com.duckduckgo.autoconsent.impl

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.duckduckgo.autoconsent.api.AutoconsentCallback
import com.duckduckgo.common.utils.plugins.PluginPoint
import org.json.JSONObject
import timber.log.Timber

class AutoconsentInterface(
    private val messageHandlerPlugins: PluginPoint<MessageHandlerPlugin>,
    private val webView: WebView,
    private val autoconsentCallback: AutoconsentCallback,
) {
    @JavascriptInterface
    fun process(message: String) {
        try {
            val parsedMessage = JSONObject(message)
            val type: String = parsedMessage.getString("type")
            messageHandlerPlugins.getPlugins().firstOrNull { it.supportedTypes.contains(type) }?.process(type, message, webView, autoconsentCallback)
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
        }
    }

    companion object {
        const val AUTOCONSENT_INTERFACE = "AutoconsentAndroid"
    }
}
