

package com.duckduckgo.app.global.model

import android.net.Uri
import android.net.http.SslCertificate
import androidx.core.net.toUri
import com.duckduckgo.app.privacy.model.HttpsStatus
import com.duckduckgo.app.surrogates.SurrogateResponse
import com.duckduckgo.app.trackerdetection.model.Entity
import com.duckduckgo.app.trackerdetection.model.TrackerStatus
import com.duckduckgo.app.trackerdetection.model.TrackingEvent
import com.duckduckgo.browser.api.brokensite.BrokenSiteContext
import com.duckduckgo.common.utils.baseHost
import com.duckduckgo.common.utils.domain

interface Site {

    /*
     * The current url for this site. This is sometimes different to the url originally
     * loaded as the url may change while loading a site
     */
    var url: String

    /*
     * The current uri for this site. This is sometimes different to the url originally
     * loaded as the url may change while loading a site
     */
    val uri: Uri?

    var title: String?
    val https: HttpsStatus
    var hasHttpResources: Boolean
    var upgradedHttps: Boolean
    var userAllowList: Boolean
    var sslError: Boolean
    var isExternalLaunch: Boolean

    val entity: Entity?
    var certificate: SslCertificate?
    val trackingEvents: List<TrackingEvent>
    val errorCodeEvents: List<String>
    val httpErrorCodeEvents: List<Int>
    val trackerCount: Int
    val otherDomainsLoadedCount: Int
    val specialDomainsLoadedCount: Int
    val majorNetworkCount: Int
    val allTrackersBlocked: Boolean
    val surrogates: List<SurrogateResponse>
    fun trackerDetected(event: TrackingEvent)
    fun onHttpErrorDetected(errorCode: Int)
    fun onErrorDetected(error: String)
    fun resetErrors()
    fun updatePrivacyData(sitePrivacyData: SitePrivacyData)
    fun surrogateDetected(surrogate: SurrogateResponse)

    fun privacyProtection(): PrivacyShield
    fun resetTrackingEvents()

    var urlParametersRemoved: Boolean
    var consentManaged: Boolean
    var consentOptOutFailed: Boolean
    var consentSelfTestFailed: Boolean
    var consentCosmeticHide: Boolean?
    var isDesktopMode: Boolean
    var nextUrl: String

    val realBrokenSiteContext: BrokenSiteContext

    var maliciousSiteStatus: MaliciousSiteStatus?

    var previousNumberOfBlockedTrackers: Int?
}

enum class MaliciousSiteStatus {
    PHISHING, MALWARE, SCAM
}

fun Site.orderedTrackerBlockedEntities(): List<Entity> = trackingEvents
    .asSequence()
    .filter { it.status == TrackerStatus.BLOCKED }
    .mapNotNull { it.entity }
    .filter { it.displayName.isNotBlank() }
    .sortedByDescending { it.prevalence }
    .toList()
    .also { previousNumberOfBlockedTrackers = it.size }

fun Site.domainMatchesUrl(matchingUrl: String): Boolean {
    return uri?.baseHost == matchingUrl.toUri().baseHost
}

fun Site.domainMatchesUrl(matchingUrl: Uri): Boolean {
    // TODO (cbarreiro) can we get rid of baseHost for the Uri as well?
    return uri?.baseHost == matchingUrl.host
}

val Site.domain get() = uri?.domain()
val Site.baseHost get() = uri?.baseHost
