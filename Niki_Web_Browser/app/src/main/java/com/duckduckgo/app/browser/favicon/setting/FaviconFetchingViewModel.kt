

package com.duckduckgo.app.browser.favicon.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.sync.api.favicons.FaviconsFetchingStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FaviconFetchingViewModel(
    private val faviconsFetchingStore: FaviconsFetchingStore,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    data class ViewState(
        val faviconsFetchingEnabled: Boolean = false,
    )

    fun viewState(): Flow<ViewState> = flowOf(
        ViewState(
            faviconsFetchingEnabled = faviconsFetchingStore.isFaviconsFetchingEnabled,
        ),
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ViewState())

    fun onFaviconFetchingSettingChanged(checked: Boolean) {
        viewModelScope.launch(dispatcherProvider.io()) {
            faviconsFetchingStore.isFaviconsFetchingEnabled = checked
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val faviconsFetchingStore: FaviconsFetchingStore,
        private val dispatcherProvider: DispatcherProvider,
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return with(modelClass) {
                when {
                    isAssignableFrom(FaviconFetchingViewModel::class.java) -> FaviconFetchingViewModel(
                        faviconsFetchingStore,
                        dispatcherProvider,
                    )

                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
        }
    }
}
