

package com.duckduckgo.privacy.config.api

/** Public interface for privacy config related callbacks **/
interface PrivacyConfigCallbackPlugin {

    /**
     * Notifies that onPrivacyConfigDownloaded event occurred.
     * This method will be called every time it downloads a new version of the privacy config.
     */
    fun onPrivacyConfigDownloaded()
}
