

package com.duckduckgo.privacy.dashboard.api.ui

interface ToggleReports {
    /**
     * Returns true if all the conditions are met for prompting the user to submit a toggle-off-prompted simplified breakage report.
     */
    suspend fun shouldPrompt(): Boolean

    /**
     * Adds a record to the datastore that a toggle-off prompt for a report was dismissed.
     */
    suspend fun onPromptDismissed()

    /**
     * Adds a record to the datastore that a report was sent as the result of a toggle-off prompt.
     */
    suspend fun onReportSent()
}
