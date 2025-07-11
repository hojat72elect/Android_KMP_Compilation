

package com.duckduckgo.app.dispatchers

import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.browser.DuckDuckGoUrlDetector
import com.duckduckgo.app.global.intentText
import com.duckduckgo.autofill.api.emailprotection.EmailProtectionLinkVerifier
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.customtabs.api.CustomTabDetector
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesViewModel(ActivityScope::class)
class IntentDispatcherViewModel @Inject constructor(
    private val customTabDetector: CustomTabDetector,
    private val dispatcherProvider: DispatcherProvider,
    private val emailProtectionLinkVerifier: EmailProtectionLinkVerifier,
    private val duckDuckGoUrlDetector: DuckDuckGoUrlDetector,
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    data class ViewState(
        val customTabRequested: Boolean = false,
        val intentText: String? = null,
        val toolbarColor: Int = 0,
        val isExternal: Boolean = false,
    )

    fun onIntentReceived(intent: Intent?, defaultColor: Int, isExternal: Boolean) {
        viewModelScope.launch(dispatcherProvider.io()) {
            runCatching {
                val hasSession = intent?.hasExtra(CustomTabsIntent.EXTRA_SESSION) == true
                val intentText = intent?.intentText
                val toolbarColor = intent?.getIntExtra(CustomTabsIntent.EXTRA_TOOLBAR_COLOR, defaultColor) ?: defaultColor
                val isEmailProtectionLink = emailProtectionLinkVerifier.shouldDelegateToInContextView(intentText, true)
                val isDuckDuckGoUrl = intentText?.let { duckDuckGoUrlDetector.isDuckDuckGoUrl(it) } ?: false
                val customTabRequested = hasSession && !isEmailProtectionLink && !isDuckDuckGoUrl

                Timber.d("Intent $intent received. Has extra session=$hasSession. Intent text=$intentText. Toolbar color=$toolbarColor")

                customTabDetector.setCustomTab(false)
                _viewState.emit(
                    viewState.value.copy(
                        customTabRequested = customTabRequested,
                        intentText = if (customTabRequested) intentText?.sanitize() else intentText,
                        toolbarColor = toolbarColor,
                        isExternal = isExternal,
                    ),
                )
            }.onFailure {
                Timber.w("Error handling custom tab intent %s", it.message)
            }
        }
    }

    private fun String.sanitize(): String {
        if (this.startsWith("http://") || this.startsWith("https://")) {
            // Some apps send URLs with spaces in the intent. This is happening mostly for authorization URLs.
            // E.g https://mastodon.social/oauth/authorize?client_id=AcfPDZlcKUjwIatVtMt8B8cmdW-w1CSOR6_rYS_6Kxs&scope=read write push&redirect_uri=mastify://oauth&response_type=code
            return this.replace(" ", "%20")
        }
        return this
    }
}
