

package com.duckduckgo.feature.toggles.api

interface FeatureTogglesInventory {
    /**
     * @return returns a list of ALL feature flags (aka [Toggle]s) currently used in the app
     */
    suspend fun getAll(): List<Toggle>

    /**
     * @return returns the list of all sub-features for a given top level feature
     */
    suspend fun getAllTogglesForParent(name: String): List<Toggle> = emptyList()

    /**
     * @return returns ALL toggles that have an assigned cohort AND "state": "enabled"
     */
    suspend fun getAllActiveExperimentToggles(): List<Toggle> = emptyList()
}
