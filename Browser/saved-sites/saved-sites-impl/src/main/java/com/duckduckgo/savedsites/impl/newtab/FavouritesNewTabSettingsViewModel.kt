

package com.duckduckgo.savedsites.impl.newtab

import android.annotation.SuppressLint
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ViewScope
import com.duckduckgo.feature.toggles.api.Toggle.State
import com.duckduckgo.savedsites.impl.SavedSitesPixelName
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("NoLifecycleObserver") // we don't observe app lifecycle
@ContributesViewModel(ViewScope::class)
class FavouritesNewTabSettingsViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val setting: NewTabFavouritesSectionSetting,
    private val pixel: Pixel,
) : ViewModel(), DefaultLifecycleObserver {

    private val _viewState = MutableStateFlow(ViewState(true))
    val viewState = _viewState.asStateFlow()

    data class ViewState(val enabled: Boolean)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        viewModelScope.launch(dispatchers.io()) {
            val isEnabled = setting.self().isEnabled()
            withContext(dispatchers.main()) {
                _viewState.update { ViewState(isEnabled) }
            }
        }
    }

    fun onSettingEnabled(enabled: Boolean) {
        setting.self().setRawStoredState(State(enabled))
        if (enabled) {
            pixel.fire(SavedSitesPixelName.FAVOURITES_SECTION_TOGGLED_ON)
        } else {
            pixel.fire(SavedSitesPixelName.FAVOURITES_SECTION_TOGGLED_OFF)
        }
    }
}
