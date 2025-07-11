

package com.duckduckgo.app.surrogates

data class SurrogateResponse(
    val scriptId: String = "",
    val responseAvailable: Boolean = true,
    val name: String = "",
    val jsFunction: String = "",
    val mimeType: String = "",
)
