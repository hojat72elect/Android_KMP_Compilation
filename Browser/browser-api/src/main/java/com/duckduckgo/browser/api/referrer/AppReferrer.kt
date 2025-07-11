

package com.duckduckgo.browser.api.referrer

/** Public interface for app referral parameters */
interface AppReferrer {

    /**
     * Sets the attribute campaign origin.
     */
    fun setOriginAttributeCampaign(origin: String?)
}
