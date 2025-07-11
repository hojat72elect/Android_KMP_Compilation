

package com.duckduckgo.autofill.api.passwordgeneration

/**
 * When password generation happens, we automatically create a login.
 * This login might be later updated with more information when the form is submitted.
 *
 * We need a way to monitor if a login was automatically created, for a specific tab, so we get data about it when form submitted.
 *
 * By design, an automatically saved login is only monitored for the current page; when a navigation event happens it will be cleared.
 */
interface AutomaticSavedLoginsMonitor {

    /**
     * Retrieves the automatically saved login ID for the current tab, if any.
     * @return the login ID, or null if no login was automatically saved for the current tab.
     */
    fun getAutoSavedLoginId(tabId: String?): Long?

    /**
     * Sets the automatically saved login ID for the current tab.
     */
    fun setAutoSavedLoginId(value: Long, tabId: String?)

    /**
     * Clears the automatically saved login ID for the current tab.
     */
    fun clearAutoSavedLoginId(tabId: String?)
}
