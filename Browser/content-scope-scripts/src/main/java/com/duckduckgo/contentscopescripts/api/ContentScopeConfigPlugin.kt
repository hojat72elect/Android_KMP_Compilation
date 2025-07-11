package com.duckduckgo.contentscopescripts.api

/**
 * Implement this interface and contribute it as a multibinding to get called upon downloading remote privacy config
 *
 * Usage:
 *
 * ```kotlin
 * @ContributesMultibinding(AppScope::class)
 * class MuFeaturePlugin @Inject constructor(...) : PrivacyFeaturePlugin {
 *
 * }
 * ```
 */
interface ContentScopeConfigPlugin {
    /**
     * @return a [String] containing the JSON config
     */
    fun config(): String

    /**
     * @return a [String] containing the user preferences.
     * Provide `null` if the feature does not have preferences.
     */
    fun preferences(): String?
}
