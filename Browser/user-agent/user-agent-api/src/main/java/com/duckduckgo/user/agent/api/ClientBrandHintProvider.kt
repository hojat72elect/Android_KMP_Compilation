

package com.duckduckgo.user.agent.api

import android.webkit.WebSettings

interface ClientBrandHintProvider {

    /**
     * Sets the client hint header SEC-CH-UA based on a specific URL and the fact that it can be an exception
     * @param settings [WebSettings] where the agent metadata will be set
     * @param url where the client hint will be set
     */
    fun setOn(settings: WebSettings?, documentUrl: String)

    /**
     * Sets the default client hint header SEC-CH-UA
     * @param settings [WebSettings] where the agent metadata will be set
     */
    fun setDefault(settings: WebSettings)

    /**
     * Checks if the url passed as parameter will force a change of branding
     * @param url where the client hint will be set
     * @return true if the branding should change
     */
    fun shouldChangeBranding(documentUrl: String): Boolean
}
