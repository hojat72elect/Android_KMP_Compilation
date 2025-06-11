

package com.duckduckgo.app.browser.model

import android.webkit.HttpAuthHandler

data class BasicAuthenticationRequest(
    val handler: HttpAuthHandler,
    val host: String,
    val realm: String,
    val site: String,
)

data class BasicAuthenticationCredentials(
    val username: String,
    val password: String,
)
