package com.duckduckgo.autoconsent.impl.handlers

import android.webkit.WebView
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.autoconsent.api.AutoconsentCallback
import com.duckduckgo.autoconsent.impl.MessageHandlerPlugin
import com.duckduckgo.autoconsent.impl.adapters.JSONObjectAdapter
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesMultibinding(AppScope::class)
class EvalMessageHandlerPlugin @Inject constructor(
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : MessageHandlerPlugin {

    private val moshi = Moshi.Builder().add(JSONObjectAdapter()).build()

    override fun process(messageType: String, jsonString: String, webView: WebView, autoconsentCallback: AutoconsentCallback) {
        if (supportedTypes.contains(messageType)) {
            appCoroutineScope.launch(dispatcherProvider.main()) {
                try {
                    val message: EvalMessage = parseMessage(jsonString) ?: return@launch
                    webView.evaluateJavascript("javascript:${script(message.code)}") {
                        val result = it.toBoolean()
                        val evalResp = EvalResp(id = message.id, result = result)
                        val response = ReplyHandler.constructReply(getMessage(evalResp))
                        webView.evaluateJavascript("javascript:$response", null)
                    }
                } catch (e: Exception) {
                    Timber.d(e.localizedMessage)
                }
            }
        }
    }

    private fun script(code: String): String {
        return """
        (function() {
            try {
                return !!(($code));
            } catch (e) {
              // ignore errors
              return;
            }
        })();
        """.trimIndent()
    }

    override val supportedTypes: List<String> = listOf("eval")

    private fun parseMessage(jsonString: String): EvalMessage? {
        val jsonAdapter: JsonAdapter<EvalMessage> = moshi.adapter(EvalMessage::class.java)
        return jsonAdapter.fromJson(jsonString)
    }

    private fun getMessage(initResp: EvalResp): String {
        val jsonAdapter: JsonAdapter<EvalResp> = moshi.adapter(EvalResp::class.java)
        return jsonAdapter.toJson(initResp).toString()
    }

    data class EvalMessage(val type: String, val id: String, val code: String)

    data class EvalResp(val type: String = "evalResp", val id: String, val result: Boolean)
}
