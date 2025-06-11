

package com.duckduckgo.savedsites.impl.sync

import androidx.lifecycle.*
import com.duckduckgo.app.global.*
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.savedsites.impl.FavoritesDisplayModeSettingsRepository
import com.duckduckgo.savedsites.store.FavoritesDisplayMode
import javax.inject.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DisplayModeViewModel(
    private val favoritesDisplayModeSettingsRepository: FavoritesDisplayModeSettingsRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    data class ViewState(
        val shareFavoritesEnabled: Boolean = false,
    )

    fun viewState(): Flow<ViewState> = favoritesDisplayModeSettingsRepository.favoritesDisplayModeFlow()
        .map { viewMode ->
            ViewState(
                shareFavoritesEnabled = viewMode == FavoritesDisplayMode.UNIFIED,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ViewState())

    fun onDisplayModeChanged(checked: Boolean) {
        viewModelScope.launch(dispatcherProvider.io()) {
            val viewMode = if (checked) FavoritesDisplayMode.UNIFIED else FavoritesDisplayMode.NATIVE
            favoritesDisplayModeSettingsRepository.favoritesDisplayMode = viewMode
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val favoritesDisplayModeSettingsRepository: FavoritesDisplayModeSettingsRepository,
        private val dispatcherProvider: DispatcherProvider,
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return with(modelClass) {
                when {
                    isAssignableFrom(DisplayModeViewModel::class.java) -> DisplayModeViewModel(
                        favoritesDisplayModeSettingsRepository,
                        dispatcherProvider,
                    )
                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
        }
    }
}
