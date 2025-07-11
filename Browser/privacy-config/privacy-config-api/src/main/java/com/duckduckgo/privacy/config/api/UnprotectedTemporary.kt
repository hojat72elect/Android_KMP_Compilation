

package com.duckduckgo.privacy.config.api

import com.duckduckgo.feature.toggles.api.FeatureException

/** Public interface for the Unprotected Temporary feature */
interface UnprotectedTemporary {
    /**
     * This method takes a [url] and returns `true` or `false` depending if the [url] is in the
     * unprotected temporary exceptions list
     * @return a `true` if the given [url] if the url is in the unprotected temporary exceptions list and `false`
     * otherwise.
     */
    fun isAnException(url: String): Boolean

    /** The unprotected temporary exceptions list */
    val unprotectedTemporaryExceptions: List<FeatureException>
}
