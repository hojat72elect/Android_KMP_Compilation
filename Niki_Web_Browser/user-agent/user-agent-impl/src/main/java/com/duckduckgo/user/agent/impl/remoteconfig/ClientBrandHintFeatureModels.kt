

package com.duckduckgo.user.agent.impl.remoteconfig

data class ClientBrandHintDomain(
    val domain: String,
    val brand: ClientBrandsHints,
)

data class ClientBrandHintSettings(
    val domains: List<ClientBrandHintDomain>,
)

/** Public enum class for the client hint brand list */
enum class ClientBrandsHints {
    DDG, CHROME, WEBVIEW;

    fun getBrand(): String {
        return when (this) {
            DDG -> "DuckDuckGo"
            CHROME -> "Google Chrome"
            WEBVIEW -> "Android WebView"
        }
    }

    companion object {
        fun from(name: String): ClientBrandsHints {
            return when (name) {
                DDG.name -> DDG
                CHROME.name -> CHROME
                WEBVIEW.name -> WEBVIEW
                else -> DDG
            }
        }
    }
}

sealed class BrandingChange {
    data object None : BrandingChange()
    data class Change(val branding: ClientBrandsHints) : BrandingChange()
}
