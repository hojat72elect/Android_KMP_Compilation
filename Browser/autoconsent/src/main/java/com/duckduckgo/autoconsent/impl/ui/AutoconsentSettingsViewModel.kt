package com.duckduckgo.autoconsent.impl.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.autoconsent.api.Autoconsent
import com.duckduckgo.autoconsent.impl.R
import com.duckduckgo.autoconsent.impl.pixels.AutoConsentPixel.SETTINGS_AUTOCONSENT_OFF
import com.duckduckgo.autoconsent.impl.pixels.AutoConsentPixel.SETTINGS_AUTOCONSENT_ON
import com.duckduckgo.autoconsent.impl.pixels.AutoConsentPixel.SETTINGS_AUTOCONSENT_SHOWN
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ContributesViewModel(ActivityScope::class)
class AutoconsentSettingsViewModel @Inject constructor(
    private val autoconsent: Autoconsent,
    private val pixel: Pixel,
) : ViewModel() {
    data class ViewState(
        val autoconsentEnabled: Boolean,
    )

    sealed class Command {
        data class LaunchLearnMoreWebPage(
            val url: String = LEARN_MORE_URL,
            @StringRes val titleId: Int = R.string.autoconsentTitle,
        ) : Command()
    }

    private val command = Channel<Command>(1, BufferOverflow.DROP_OLDEST)

    private val viewStateFlow: MutableStateFlow<ViewState> =
        MutableStateFlow(ViewState(autoconsent.isSettingEnabled()))
    val viewState: StateFlow<ViewState> = viewStateFlow

    init {
        pixel.fire(SETTINGS_AUTOCONSENT_SHOWN)
    }

    fun commands(): Flow<Command> {
        return command.receiveAsFlow()
    }

    fun onUserToggleAutoconsent(enabled: Boolean) {
        viewModelScope.launch {
            pixel.fire(
                if (enabled) {
                    SETTINGS_AUTOCONSENT_ON
                } else {
                    SETTINGS_AUTOCONSENT_OFF
                },
            )
            autoconsent.changeSetting(enabled)
            viewStateFlow.emit(ViewState(autoconsent.isSettingEnabled()))
        }
    }

    fun onLearnMoreSelected() {
        viewModelScope.launch { command.send(Command.LaunchLearnMoreWebPage()) }
    }

    companion object {
        const val LEARN_MORE_URL = "https://help.duckduckgo.com/duckduckgo-help-pages/privacy/web-tracking-protections/#cookie-pop-up-management"
    }
}
