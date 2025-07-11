

package com.duckduckgo.common.utils.plugins.pixel

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

/**
 * A plugin point for pixels that require data cleaning.
 * Pixels specified through this mechanism will have their ATB and App version information removed before pixel is sent.
 */
@ContributesPluginPoint(AppScope::class)
interface PixelParamRemovalPlugin {
    /**
     * List of pixels (pixel name or prefix) for which the provided set of parameters will be removed.
     */
    fun names(): List<Pair<String, Set<PixelParameter>>>

    enum class PixelParameter {
        ATB, APP_VERSION;

        companion object {
            fun removeAll() = setOf(ATB, APP_VERSION)
            fun removeAtb() = setOf(ATB)
            fun removeVersion() = setOf(APP_VERSION)
        }
    }
}
