

package com.duckduckgo.experiments.api

data class VariantConfig(
    val variantKey: String,
    val weight: Double? = 0.0,
    val filters: VariantFilters? = null,
)

data class VariantFilters(
    val locale: List<String> = emptyList(),
    val androidVersion: List<String> = emptyList(),
    val privacyProEligible: Boolean? = null,
)
