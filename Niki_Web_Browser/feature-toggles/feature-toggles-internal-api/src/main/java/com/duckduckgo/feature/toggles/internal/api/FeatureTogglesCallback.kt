
package com.duckduckgo.feature.toggles.internal.api

/**
 * This interface exists to facilitate the implementation of ToggleImpl which contains logic inside an api module.
 * This is an internal implementation to thread the need between toggles-api and toggles-impl and should NEVER
 * be used publicly.
 */
interface FeatureTogglesCallback {

    /**
     * This method is called whenever a cohort is assigned to the FeatureToggle
     */
    fun onCohortAssigned(experimentName: String, cohortName: String, enrollmentDate: String)

    /**
     * @return `true` if the ANY of the remote feature targets match the device configuration, `false` otherwise
     */
    fun matchesToggleTargets(targets: List<Any>): Boolean
}
