

package com.duckduckgo.cookies.impl

import com.duckduckgo.common.utils.AppUrl
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.cookies.api.CookieManagerProvider
import com.duckduckgo.cookies.api.DuckDuckGoCookieManager
import com.duckduckgo.cookies.api.RemoveCookiesStrategy
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber

@ContributesBinding(AppScope::class)
class WebViewCookieManager @Inject constructor(
    private val cookieManager: CookieManagerProvider,
    private val removeCookies: RemoveCookiesStrategy,
    private val dispatcher: DispatcherProvider,
) : DuckDuckGoCookieManager {

    override suspend fun removeExternalCookies() {
        withContext(dispatcher.io()) {
            flush()
        }
        // The Fire Button does not delete the user's DuckDuckGo search settings, which are saved as cookies.
        // Removing these cookies would reset them and have undesired consequences, i.e. changing the theme, default language, etc.
        // The Fire Button also does not delete temporary cookies associated with 'surveys.duckduckgo.com'.
        // When we launch surveys to help us understand issues that impact users over time, we use this cookie to temporarily store anonymous
        // survey answers, before deleting the cookie. Cookie storage duration is communicated to users before they opt to submit survey answers.
        // These cookies are not stored in a personally identifiable way. For example, the large size setting is stored as 's=l.'
        // More info in https://duckduckgo.com/privacy
        val ddgCookies = getDuckDuckGoCookies()
        if (cookieManager.get()?.hasCookies() == true) {
            removeCookies.removeCookies()
            storeDuckDuckGoCookies(ddgCookies)
        }
        withContext(dispatcher.io()) {
            flush()
        }
    }

    private suspend fun storeDuckDuckGoCookies(cookies: Map<String, List<String>>) {
        cookies.keys.forEach { host ->
            cookies[host]?.forEach {
                val cookie = it.trim()
                Timber.d("Restoring DDB cookie: $cookie")
                storeCookie(cookie, host)
            }
        }
    }

    private suspend fun storeCookie(cookie: String, host: String) {
        suspendCoroutine { continuation ->
            cookieManager.get()?.setCookie(host, cookie) { success ->
                Timber.v("Cookie $cookie stored successfully: $success")
                continuation.resume(Unit)
            }
        }
    }

    private fun getDuckDuckGoCookies(): Map<String, List<String>> {
        val map = mutableMapOf<String, List<String>>()
        DDG_COOKIE_DOMAINS.forEach { host ->
            map[host] = cookieManager.get()?.getCookie(host)?.split(";").orEmpty()
        }
        return map
    }

    override fun flush() {
        cookieManager.get()?.flush()
    }

    companion object {
        val DDG_COOKIE_DOMAINS = listOf(AppUrl.Url.COOKIES, AppUrl.Url.SURVEY_COOKIES)
    }
}
