

package com.duckduckgo.app.brokensite

import androidx.core.net.toUri
import com.duckduckgo.app.browser.DuckDuckGoUrlDetector
import com.duckduckgo.browser.api.brokensite.BrokenSiteContext
import com.duckduckgo.browser.api.brokensite.BrokenSiteOpenerContext
import com.duckduckgo.common.utils.isHttp
import com.duckduckgo.common.utils.isHttps
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import org.json.JSONArray
import timber.log.Timber

@ContributesBinding(AppScope::class)
class RealBrokenSiteContext @Inject constructor(
    private val duckDuckGoUrlDetector: DuckDuckGoUrlDetector,
) : BrokenSiteContext {

    override var userRefreshCount: Int = 0

    override var openerContext: BrokenSiteOpenerContext? = null

    override var jsPerformance: DoubleArray? = null

    override fun onUserTriggeredRefresh() {
        userRefreshCount++
    }

    override fun inferOpenerContext(
        referrer: String?,
        isExternalLaunch: Boolean,
    ) {
        if (isExternalLaunch) {
            openerContext = BrokenSiteOpenerContext.EXTERNAL
        } else if (referrer != null) {
            openerContext = when {
                duckDuckGoUrlDetector.isDuckDuckGoUrl(referrer) -> BrokenSiteOpenerContext.SERP
                referrer.toUri().isHttp || referrer.toUri().isHttps -> BrokenSiteOpenerContext.NAVIGATION
                else -> null
            }
            Timber.d(
                "openerContext inferred -> ${openerContext?.context}",
            )
        } else {
            Timber.d("openerContext not inferred because referrer is null")
        }
    }

    override fun recordJsPerformance(performanceMetrics: JSONArray) {
        val recordedJsValues = DoubleArray(performanceMetrics.length())
        for (i in 0 until performanceMetrics.length()) {
            recordedJsValues[i] = performanceMetrics.getDouble(i)
        }
        jsPerformance = recordedJsValues
        Timber.d("jsPerformance recorded as $performanceMetrics")
    }
}
