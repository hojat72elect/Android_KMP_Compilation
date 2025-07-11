

package com.duckduckgo.app.icon.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.icon.api.AppIcon
import com.duckduckgo.app.icon.api.IconModifier
import com.duckduckgo.app.pixels.AppPixelName
import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.SingleLiveEvent
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject

@ContributesViewModel(ActivityScope::class)
class ChangeIconViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val appIconModifier: IconModifier,
    private val pixel: Pixel,
) : ViewModel() {

    data class IconViewData(
        val appIcon: AppIcon,
        val selected: Boolean,
    ) {
        companion object {
            fun from(
                appIcon: AppIcon,
                selectedAppIcon: AppIcon,
            ): IconViewData {
                return if (appIcon.componentName == selectedAppIcon.componentName) {
                    IconViewData(appIcon, true)
                } else {
                    IconViewData(appIcon, false)
                }
            }
        }
    }

    data class ViewState(
        val appIcons: List<IconViewData>,
    )

    sealed class Command {
        object IconChanged : Command()
        data class ShowConfirmationDialog(val viewData: IconViewData) : Command()
    }

    val viewState: MutableLiveData<ViewState> = MutableLiveData()
    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    fun start() {
        pixel.fire(AppPixelName.CHANGE_APP_ICON_OPENED)
        val selectedIcon = settingsDataStore.appIcon
        viewState.value = ViewState(AppIcon.values().map { IconViewData.from(it, selectedIcon) })
    }

    fun onIconSelected(viewData: IconViewData) {
        command.value = Command.ShowConfirmationDialog(viewData)
    }

    fun onIconConfirmed(viewData: IconViewData) {
        val previousIcon = settingsDataStore.appIcon
        settingsDataStore.appIcon = viewData.appIcon
        settingsDataStore.appIconChanged = true
        appIconModifier.changeIcon(previousIcon, viewData.appIcon)
        command.value = Command.IconChanged
    }
}
