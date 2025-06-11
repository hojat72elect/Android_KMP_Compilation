

package com.duckduckgo.macos.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.macos.impl.MacOsPixelNames.MACOS_WAITLIST_SHARE_PRESSED
import com.duckduckgo.macos.impl.MacOsViewModel.Command.GoToWindowsClientSettings
import com.duckduckgo.macos.impl.MacOsViewModel.Command.ShareLink
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ContributesViewModel(AppScope::class)
class MacOsViewModel @Inject constructor(
    private val pixel: Pixel,
    private val macOsDownloadLinkOrigin: MacOsDownloadLinkOrigin,
) : ViewModel() {

    private val commandChannel = Channel<Command>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val commands = commandChannel.receiveAsFlow()

    sealed class Command {
        data class ShareLink(val originEnabled: Boolean) : Command()
        object GoToWindowsClientSettings : Command()
    }

    data class ViewState(val windowsFeatureEnabled: Boolean)

    fun onShareClicked() {
        viewModelScope.launch {
            commandChannel.send(ShareLink(originEnabled = macOsDownloadLinkOrigin.self().isEnabled()))
            pixel.fire(MACOS_WAITLIST_SHARE_PRESSED)
        }
    }

    fun onGoToWindowsClicked() {
        viewModelScope.launch {
            commandChannel.send(GoToWindowsClientSettings)
        }
    }
}
