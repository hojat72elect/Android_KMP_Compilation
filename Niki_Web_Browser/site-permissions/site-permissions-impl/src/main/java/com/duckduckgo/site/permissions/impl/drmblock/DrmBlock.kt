

package com.duckduckgo.site.permissions.impl.drmblock

/** Interface for the DRM blocking feature */
interface DrmBlock {
    /**
     * This method takes a [url] of the page requesting DRM permissions
     * @return `true` if the given [url] is not allowed to ask for DRM,
     * `false` otherwise.
     */
    fun isDrmBlockedForUrl(
        url: String,
    ): Boolean
}
