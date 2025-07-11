package com.duckduckgo.customtabs.api

/**
 * Interface for detecting if a tab is a custom tab.
 */
interface CustomTabDetector {
    /**
     * Checks if the current tab is a custom tab.
     *
     * @return true if the current tab is a custom tab, false otherwise.
     */
    fun isCustomTab(): Boolean

    /**
     * Sets the current tab as a custom tab or a regular tab.
     *
     * @param value true to set the current tab as a custom tab, false to set it as a regular tab.
     */
    fun setCustomTab(value: Boolean)
}
