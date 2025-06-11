

package com.duckduckgo.app.statistics.store

import com.duckduckgo.app.statistics.model.Atb

/**
 * Persistent storage for ATB values.
 */
interface StatisticsDataStore {

    val hasInstallationStatistics: Boolean

    var atb: Atb?
    var appRetentionAtb: String?
    var searchRetentionAtb: String?
    var variant: String?
    var referrerVariant: String?

    fun saveAtb(atb: Atb)
    fun clearAtb()
}
