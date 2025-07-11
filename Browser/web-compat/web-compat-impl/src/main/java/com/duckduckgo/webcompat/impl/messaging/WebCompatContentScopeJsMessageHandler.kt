

package com.duckduckgo.webcompat.impl.messaging

import com.duckduckgo.contentscopescripts.api.ContentScopeJsMessageHandlersPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.js.messaging.api.JsMessage
import com.duckduckgo.js.messaging.api.JsMessageCallback
import com.duckduckgo.js.messaging.api.JsMessageHandler
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class WebCompatContentScopeJsMessageHandler @Inject constructor() : ContentScopeJsMessageHandlersPlugin {

    override fun getJsMessageHandler(): JsMessageHandler = object : JsMessageHandler {
        override fun process(
            jsMessage: JsMessage,
            secret: String,
            jsMessageCallback: JsMessageCallback?,
        ) {
            if (jsMessage.id == null) return
            jsMessageCallback?.process(featureName, jsMessage.method, jsMessage.id, jsMessage.params)
        }

        override val allowedDomains: List<String> = emptyList()
        override val featureName: String = "webCompat"
        override val methods: List<String> = listOf("webShare", "permissionsQuery", "screenLock", "screenUnlock")
    }
}
