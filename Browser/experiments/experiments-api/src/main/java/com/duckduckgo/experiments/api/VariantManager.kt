

package com.duckduckgo.experiments.api

/** Public interface for variant manager feature*/
interface VariantManager {
    /**
     * Returns the default variant key
     */
    fun defaultVariantKey(): String

    /**
     * Returns the variant key assigned to the user
     */
    fun getVariantKey(): String?

    /**
     * Updates user experimental variant when referralResultReceived from PlayStore
     */
    fun updateAppReferrerVariant(variant: String)

    /**
     * Updated experimental variants received
     *
     * @param variants Updated list of VariantConfig objects
     */
    fun updateVariants(variants: List<VariantConfig>)
}
