package com.duckduckgo.contentscopescripts.api

import com.duckduckgo.js.messaging.api.JsMessageHandler

/**
 * Implement this interface and contribute it as a multibinding to manage JS Messages that are sent to C-S-S
 */
interface ContentScopeJsMessageHandlersPlugin {
    /**
     * @return a [JsMessageHandler] that will be used to handle the JS messages
     */
    fun getJsMessageHandler(): JsMessageHandler
}
