package com.duckduckgo.autoconsent.impl.handlers

object ReplyHandler {
    fun constructReply(message: String): String {
        return """
            (function() {
                window.autoconsentMessageCallback($message, window.origin);
            })();
        """.trimIndent()
    }
}
