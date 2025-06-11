

package com.duckduckgo.request.filterer.impl

import android.webkit.WebResourceRequest
import androidx.core.net.toUri
import com.duckduckgo.app.browser.UriString
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureToggle
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.duckduckgo.request.filterer.api.RequestFilterer
import com.duckduckgo.request.filterer.api.RequestFiltererFeatureName
import com.duckduckgo.request.filterer.store.RequestFiltererRepository
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber

@ContributesBinding(AppScope::class)
class RequestFiltererImpl @Inject constructor(
    private val repository: RequestFiltererRepository,
    private val toggle: FeatureToggle,
    private val unprotectedTemporary: UnprotectedTemporary,
    val dispatcherProvider: DispatcherProvider,
) : RequestFilterer {

    private val scope = CoroutineScope(dispatcherProvider.io())
    private val windowInMs = repository.settings.windowInMs.toLong()

    var job: Job? = null
    private var previousPage: String? = null
    private var currentPage: String? = null
    private var hasTimeElapsed: Boolean = true

    override fun shouldFilterOutRequest(request: WebResourceRequest, documentUrl: String?): Boolean {
        if (documentUrl.isNullOrEmpty()) return false
        if (!isFeatureEnabled()) return false
        if (hasTimeElapsed) return false
        if (isAnException(documentUrl)) return false

        val origin = request.requestHeaders[ORIGIN]
        val referer = request.requestHeaders[REFERER]

        runCatching {
            val currentTopDomain = documentUrl.toHttpUrl().topPrivateDomain()
            val previousTopDomain = previousPage?.toHttpUrl()?.topPrivateDomain()

            if (currentTopDomain != previousTopDomain) {
                referer?.let {
                    return compareUrl(it)
                }
                origin?.let {
                    return compareUrl(it)
                }
            }
        }
        return false
    }

    override fun registerOnPageCreated(url: String) {
        previousPage = currentPage
        currentPage = url
        hasTimeElapsed = false
        if (job?.isActive == true) job?.cancel()
        job = scope.launch(dispatcherProvider.io()) {
            delay(windowInMs)
            hasTimeElapsed = true
        }
    }

    private fun compareUrl(url: String): Boolean {
        return try {
            val uri = url.toUri()
            val previousUri = previousPage?.toUri()
            if (uri.path.isNullOrEmpty()) {
                return uri.host == previousUri?.host
            }
            return url == previousPage
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
            false
        }
    }

    private fun isAnException(url: String): Boolean {
        return unprotectedTemporary.isAnException(url) || matches(url)
    }

    private fun matches(url: String): Boolean {
        return repository.exceptions.any { UriString.sameOrSubdomain(url, it.domain) }
    }
    private fun isFeatureEnabled(): Boolean = toggle.isFeatureEnabled(RequestFiltererFeatureName.RequestFilterer.value)

    companion object {
        const val ORIGIN = "Origin"
        const val REFERER = "Referer"
    }
}
