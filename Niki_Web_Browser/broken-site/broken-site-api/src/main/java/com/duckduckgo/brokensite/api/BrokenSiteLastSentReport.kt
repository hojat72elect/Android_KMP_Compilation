

package com.duckduckgo.brokensite.api

interface BrokenSiteLastSentReport {

    /**
     * Retrieves the date when a broken site report was last sent for the specified hostname. The 'broken_site_last_sent_report' table in
     * the 'broken_site' database stores this date.
     * @return a string containing the date in the format 'yyyy-MM-dd', or null if a report was not sent in the last 30 days.
     */
    suspend fun getLastSentDay(hostname: String): String?

    /**
     * Sets / updates the last sent date for a broken site report associated with the provided hostname, setting it to the current date.
     * The 'broken_site_last_sent_report' table in the 'broken_site' database stores this updated date along with a prefix
     * of the hashed hostname.
     */
    fun setLastSentDay(hostname: String)
}
