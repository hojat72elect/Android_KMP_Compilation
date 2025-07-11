

package com.duckduckgo.app.browser.customtabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.customtabs.api.CustomTabDetector
import com.duckduckgo.di.scopes.ActivityScope
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesViewModel(ActivityScope::class)
class CustomTabViewModel @Inject constructor(
    private val customTabDetector: CustomTabDetector,
    private val pixel: Pixel,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    data class ViewState(
        val tabId: String = "${CUSTOM_TAB_NAME_PREFIX}${UUID.randomUUID()}",
        val url: String? = null,
        val toolbarColor: Int = 0,
    )

    fun onCustomTabCreated(url: String?, toolbarColor: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            _viewState.emit(
                viewState.value.copy(
                    url = url,
                    toolbarColor = toolbarColor,
                ),
            )
            pixel.fire(CustomTabPixelNames.CUSTOM_TABS_OPENED)
        }
    }

    fun onShowCustomTab() {
        Timber.d("Show Custom Tab with tabId=${viewState.value.tabId}")
        customTabDetector.setCustomTab(true)
    }

    fun onCloseCustomTab() {
        Timber.d("Close Custom Tab with tabId=${viewState.value.tabId}")
        customTabDetector.setCustomTab(false)
    }

    companion object {
        const val CUSTOM_TAB_NAME_PREFIX = "CustomTab-"
    }
}
