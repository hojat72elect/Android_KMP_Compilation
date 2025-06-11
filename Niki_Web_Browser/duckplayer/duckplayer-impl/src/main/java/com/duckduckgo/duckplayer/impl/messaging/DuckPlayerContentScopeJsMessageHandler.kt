

package com.duckduckgo.duckplayer.impl.messaging

import com.duckduckgo.common.utils.AppUrl
import com.duckduckgo.contentscopescripts.api.ContentScopeJsMessageHandlersPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.duckplayer.api.YOUTUBE_HOST
import com.duckduckgo.duckplayer.api.YOUTUBE_MOBILE_HOST
import com.duckduckgo.js.messaging.api.JsMessage
import com.duckduckgo.js.messaging.api.JsMessageCallback
import com.duckduckgo.js.messaging.api.JsMessageHandler
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class DuckPlayerContentScopeJsMessageHandler @Inject constructor() : ContentScopeJsMessageHandlersPlugin {
    override fun getJsMessageHandler(): JsMessageHandler = object : JsMessageHandler {
        override fun process(jsMessage: JsMessage, secret: String, jsMessageCallback: JsMessageCallback?) {
            jsMessageCallback?.process(featureName, jsMessage.method, jsMessage.id ?: "", jsMessage.params)
        }

        override val allowedDomains: List<String> = listOf(
            AppUrl.Url.HOST,
            YOUTUBE_HOST,
            YOUTUBE_MOBILE_HOST,
        )

        override val featureName: String = "duckPlayer"
        override val methods: List<String> = listOf(
            "getUserValues",
            "sendDuckPlayerPixel",
            "setUserValues",
            "openDuckPlayer",
            "openInfo",
            "initialSetup",
            "reportPageException",
            "reportInitException",
        )
    }
}
