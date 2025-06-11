

package com.duckduckgo.duckchat.impl.messaging

import com.duckduckgo.common.utils.AppUrl
import com.duckduckgo.contentscopescripts.api.ContentScopeJsMessageHandlersPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.js.messaging.api.JsMessage
import com.duckduckgo.js.messaging.api.JsMessageCallback
import com.duckduckgo.js.messaging.api.JsMessageHandler
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class DuckChatContentScopeJsMessageHandler @Inject constructor() : ContentScopeJsMessageHandlersPlugin {
    override fun getJsMessageHandler(): JsMessageHandler = object : JsMessageHandler {
        override fun process(jsMessage: JsMessage, secret: String, jsMessageCallback: JsMessageCallback?) {
            jsMessageCallback?.process(featureName, jsMessage.method, jsMessage.id ?: "", jsMessage.params)
        }

        override val allowedDomains: List<String> = listOf(
            AppUrl.Url.HOST,
        )

        override val featureName: String = "aiChat"
        override val methods: List<String> = listOf(
            "getAIChatNativeHandoffData",
            "getAIChatNativeConfigValues",
            "openAIChat",
            "closeAIChat",
            "openAIChatSettings",
            "responseState",
            "hideChatInput",
            "showChatInput",
        )
    }
}
