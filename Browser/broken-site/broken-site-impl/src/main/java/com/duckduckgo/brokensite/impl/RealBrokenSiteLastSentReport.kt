

package com.duckduckgo.brokensite.impl

import com.duckduckgo.brokensite.api.BrokenSiteLastSentReport
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealBrokenSiteLastSentReport @Inject constructor(
    private val brokenSiteReportRepository: BrokenSiteReportRepository,
) : BrokenSiteLastSentReport {

    override suspend fun getLastSentDay(hostname: String): String? {
        return brokenSiteReportRepository.getLastSentDay(hostname)
    }

    override fun setLastSentDay(hostname: String) {
        brokenSiteReportRepository.setLastSentDay(hostname)
    }
}
