

package com.duckduckgo.privacyprotectionspopup.api

interface PrivacyProtectionsToggleUsageListener {

    /**
     * Should be invoked whenever user manually toggles privacy protections for a site.
     * Popup trigger heuristic is impacted by the use of privacy protections toggle.
     */
    suspend fun onPrivacyProtectionsToggleUsed()
}
