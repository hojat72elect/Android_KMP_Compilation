

package com.duckduckgo.app.fakes

import android.net.Uri
import android.webkit.WebResourceRequest
import com.duckduckgo.app.browser.webview.MaliciousSiteBlockerWebViewIntegration
import com.duckduckgo.app.browser.webview.RealMaliciousSiteBlockerWebViewIntegration.IsMaliciousViewData
import com.duckduckgo.app.browser.webview.RealMaliciousSiteBlockerWebViewIntegration.IsMaliciousViewData.Safe
import com.duckduckgo.malicioussiteprotection.api.MaliciousSiteProtection.Feed
import com.duckduckgo.malicioussiteprotection.api.MaliciousSiteProtection.MaliciousStatus

class FakeMaliciousSiteBlockerWebViewIntegration : MaliciousSiteBlockerWebViewIntegration {
    override suspend fun shouldIntercept(
        request: WebResourceRequest,
        documentUri: Uri?,
        confirmationCallback: (maliciousStatus: MaliciousStatus) -> Unit,
    ): IsMaliciousViewData {
        return Safe(request.isForMainFrame)
    }

    override fun shouldOverrideUrlLoading(
        url: Uri,
        isForMainFrame: Boolean,
        confirmationCallback: (maliciousStatus: MaliciousStatus) -> Unit,
    ): IsMaliciousViewData {
        return Safe(isForMainFrame)
    }

    override fun onPageLoadStarted(url: String) {
        // no-op
    }

    override fun onSiteExempted(
        url: Uri,
        feed: Feed,
    ) {
        TODO("Not yet implemented")
    }
}
