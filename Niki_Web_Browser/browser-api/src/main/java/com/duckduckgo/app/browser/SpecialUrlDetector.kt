

package com.duckduckgo.app.browser

import android.content.Intent
import android.net.Uri

interface SpecialUrlDetector {
    fun determineType(initiatingUrl: String?, uri: Uri): UrlType
    fun determineType(uriString: String?): UrlType
    fun processUrl(initiatingUrl: String?, uriString: String): UrlType

    sealed class UrlType {
        class Web(val webAddress: String) : UrlType()
        class Telephone(val telephoneNumber: String) : UrlType()
        class Email(val emailAddress: String) : UrlType()
        class Sms(val telephoneNumber: String) : UrlType()
        class AppLink(
            val appIntent: Intent? = null,
            val uriString: String,
        ) : UrlType()

        class NonHttpAppLink(
            val uriString: String,
            val intent: Intent,
            val fallbackUrl: String?,
            val fallbackIntent: Intent? = null,
        ) : UrlType()

        class SearchQuery(val query: String) : UrlType()
        class Unknown(val uriString: String) : UrlType()
        class ExtractedAmpLink(val extractedUrl: String) : UrlType()
        class CloakedAmpLink(val ampUrl: String) : UrlType()
        class TrackingParameterLink(val cleanedUrl: String) : UrlType()
        data object ShouldLaunchPrivacyProLink : UrlType()
        data class ShouldLaunchDuckPlayerLink(val url: Uri) : UrlType()
        class DuckScheme(val uriString: String) : UrlType()
        data object ShouldLaunchDuckChatLink : UrlType()
    }
}
