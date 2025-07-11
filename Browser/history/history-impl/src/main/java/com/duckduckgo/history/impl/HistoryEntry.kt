

package com.duckduckgo.history.impl

import androidx.core.net.toUri
import com.duckduckgo.history.api.HistoryEntry
import com.duckduckgo.history.api.HistoryEntry.VisitedPage
import com.duckduckgo.history.api.HistoryEntry.VisitedSERP
import com.duckduckgo.history.impl.store.HistoryEntryWithVisits
import java.time.LocalDateTime

fun HistoryEntryWithVisits.toHistoryEntry(): HistoryEntry? {
    if (historyEntry.url.isBlank()) return null
    return if (historyEntry.isSerp && !historyEntry.query.isNullOrBlank()) {
        VisitedSERP(historyEntry.url.toUri(), historyEntry.title, historyEntry.query, visits = visits.map { LocalDateTime.parse(it.timestamp) })
    } else {
        VisitedPage(historyEntry.url.toUri(), historyEntry.title, visits.map { LocalDateTime.parse(it.timestamp) })
    }
}
