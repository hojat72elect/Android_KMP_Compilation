

package com.duckduckgo.browser.api.brokensite

import org.json.JSONArray

interface BrokenSiteContext {
    var userRefreshCount: Int

    var openerContext: BrokenSiteOpenerContext?

    var jsPerformance: DoubleArray?

    fun onUserTriggeredRefresh()
    fun inferOpenerContext(
        referrer: String?,
        wasLaunchedExternally: Boolean,
    )
    fun recordJsPerformance(performanceMetrics: JSONArray)
}
