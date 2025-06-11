

package com.duckduckgo.privacy.config.api

/** Public interface for the Tracking Parameters feature */
interface TrackingParameters {
    /**
     * This method takes an optional [initiatingUrl] and a [url] and returns a [String] containing the cleaned URL with tracking parameters removed.
     * @return the URL [String] or `null` if the [url] does not contain tracking parameters.
     */
    fun cleanTrackingParameters(initiatingUrl: String?, url: String): String?

    /** The last tracking parameter cleaned URL. */
    var lastCleanedUrl: String?
}
