

package com.duckduckgo.autofill.impl.ui.credential.saving.declines

interface AutofillDeclineStore {
    /**
     * Whether to monitor autofill decline counts or not
     * Used to determine whether we should actively detect when a user new to autofill doesn't appear to want it enabled
     */
    var monitorDeclineCounts: Boolean

    /**
     * A count of the number of autofill declines the user has made, persisted across all sessions.
     * Used to determine whether we should prompt a user new to autofill to disable it if they don't appear to want it enabled
     */
    var autofillDeclineCount: Int
}
