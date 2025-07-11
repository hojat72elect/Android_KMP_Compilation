package com.duckduckgo.autoconsent.impl.handlers

import android.webkit.WebView
import com.duckduckgo.autoconsent.api.AutoconsentCallback
import com.duckduckgo.autoconsent.impl.MessageHandlerPlugin
import com.duckduckgo.autoconsent.impl.adapters.JSONObjectAdapter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import timber.log.Timber

@ContributesMultibinding(AppScope::class)
class SelfTestResultMessageHandlerPlugin @Inject constructor() : MessageHandlerPlugin {

    private val moshi = Moshi.Builder().add(JSONObjectAdapter()).build()

    override fun process(messageType: String, jsonString: String, webView: WebView, autoconsentCallback: AutoconsentCallback) {
        if (supportedTypes.contains(messageType)) {
            try {
                val message: SelfTestResultMessage = parseMessage(jsonString) ?: return
                autoconsentCallback.onResultReceived(consentManaged = true, optOutFailed = false, selfTestFailed = message.result, isCosmetic = null)
            } catch (e: Exception) {
                Timber.d(e.localizedMessage)
            }
        }
    }

    override val supportedTypes: List<String> = listOf("selfTestResult")

    private fun parseMessage(jsonString: String): SelfTestResultMessage? {
        val jsonAdapter: JsonAdapter<SelfTestResultMessage> = moshi.adapter(SelfTestResultMessage::class.java)
        return jsonAdapter.fromJson(jsonString)
    }

    data class SelfTestResultMessage(val type: String, val cmp: String, val result: Boolean, val url: String)
}
