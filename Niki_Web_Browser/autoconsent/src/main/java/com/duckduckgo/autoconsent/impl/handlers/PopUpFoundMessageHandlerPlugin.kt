package com.duckduckgo.autoconsent.impl.handlers

import android.webkit.WebView
import com.duckduckgo.autoconsent.api.AutoconsentCallback
import com.duckduckgo.autoconsent.impl.MessageHandlerPlugin
import com.duckduckgo.autoconsent.impl.adapters.JSONObjectAdapter
import com.duckduckgo.autoconsent.impl.store.AutoconsentSettingsRepository
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import timber.log.Timber

@ContributesMultibinding(AppScope::class)
class PopUpFoundMessageHandlerPlugin @Inject constructor(private val repository: AutoconsentSettingsRepository) : MessageHandlerPlugin {

    private val moshi = Moshi.Builder().add(JSONObjectAdapter()).build()

    override fun process(messageType: String, jsonString: String, webView: WebView, autoconsentCallback: AutoconsentCallback) {
        try {
            if (supportedTypes.contains(messageType)) {
                val message: PopUpFoundMessage = parseMessage(jsonString) ?: return
                if (repository.userSetting) return
                if (message.cmp.endsWith(IGNORE_CMP_SUFFIX, ignoreCase = true)) return

                autoconsentCallback.onFirstPopUpHandled()
            }
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
        }
    }

    override val supportedTypes: List<String> = listOf("popupFound")

    private fun parseMessage(jsonString: String): PopUpFoundMessage? {
        val jsonAdapter: JsonAdapter<PopUpFoundMessage> = moshi.adapter(PopUpFoundMessage::class.java)
        return jsonAdapter.fromJson(jsonString)
    }

    data class PopUpFoundMessage(val type: String, val cmp: String, val url: String)

    companion object {
        private const val IGNORE_CMP_SUFFIX = "-top"
    }
}
