

package com.duckduckgo.experiments.impl

import com.duckduckgo.appbuildconfig.api.AppBuildConfig

/**
 * A variant which can be used for experimentation.
 * @param weight Relative weight. These are normalised to all other variants, so they don't have to add up to any specific number.
 *
 */
data class Variant(
    val key: String,
    override val weight: Double = 0.0,
    val filterBy: (config: AppBuildConfig) -> Boolean,
) : Probabilistic
