

package com.duckduckgo.app.browser.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ContributesViewModel(ActivityScope::class)
class WebViewDevSettingsViewModel @Inject constructor(
    private val webViewInformationExtractor: WebViewInformationExtractor,
) : ViewModel() {

    data class ViewState(
        val webViewVersion: String = "unknown",
        val webViewPackage: String = "unknown",
    )

    private val viewState = MutableStateFlow(ViewState())

    fun viewState(): StateFlow<ViewState> {
        return viewState
    }

    fun start() {
        viewModelScope.launch {
            val webViewData = webViewInformationExtractor.extract()

            viewState.emit(
                currentViewState().copy(
                    webViewVersion = webViewData.webViewVersion,
                    webViewPackage = webViewData.webViewPackageName,
                ),
            )
        }
    }

    private fun currentViewState(): ViewState {
        return viewState.value
    }
}
