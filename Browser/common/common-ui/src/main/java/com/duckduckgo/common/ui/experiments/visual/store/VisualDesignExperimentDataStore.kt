

package com.duckduckgo.common.ui.experiments.visual.store

import kotlinx.coroutines.flow.StateFlow

interface VisualDesignExperimentDataStore {

    /**
     * State flow which returns `true` if the feature flag for the experiment is enabled and there are no conflicting experiments detected.
     */
    val isExperimentEnabled: StateFlow<Boolean>

    /**
     * State flow which returns `true` if the feature flag for the Duck AI PoC is enabled and there are no conflicting experiments detected.
     */
    val isDuckAIPoCEnabled: StateFlow<Boolean>

    /**
     * State flow which returns `true` if there are any conflicting experiments detected.
     */
    val anyConflictingExperimentEnabled: StateFlow<Boolean>

    fun changeExperimentFlagPreference(enabled: Boolean)
    fun changeDuckAIPoCFlagPreference(enabled: Boolean)
}
