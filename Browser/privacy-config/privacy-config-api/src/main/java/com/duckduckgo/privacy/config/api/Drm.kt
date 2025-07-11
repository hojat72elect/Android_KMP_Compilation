

package com.duckduckgo.privacy.config.api

/** Public interface for the DRM feature */
interface Drm {
    /**
     * This method takes a [url] of the page requesting DRM permissions
     * @return an `true` if the given [url] is allowed DRM, `false`
     * otherwise.
     */
    fun isDrmAllowedForUrl(
        url: String,
    ): Boolean
}
