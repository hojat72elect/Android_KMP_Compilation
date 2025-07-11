

package com.duckduckgo.app.browser.remotemessage

import com.duckduckgo.app.pixels.AppPixelName
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ViewScope
import com.duckduckgo.remote.messaging.api.Action
import com.duckduckgo.remote.messaging.api.Content
import com.duckduckgo.remote.messaging.api.RemoteMessage
import com.duckduckgo.remote.messaging.api.RemoteMessagingRepository
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext

@SingleInstanceIn(ViewScope::class)
class RemoteMessagingViewModel @Inject constructor(
    private val remoteMessagingRepository: RemoteMessagingRepository,
    private val pixel: Pixel,
    private val dispatchers: DispatcherProvider,
) {

    val activeMessages = remoteMessagingRepository.messageFlow()

    suspend fun onMessageShown(remoteMessage: RemoteMessage) {
        withContext(dispatchers.io()) {
            tryToFireUniquePixel(remoteMessage)
            pixel.fire(AppPixelName.REMOTE_MESSAGE_SHOWN, remoteMessage.asPixelParams())
        }
    }

    private fun tryToFireUniquePixel(remoteMessage: RemoteMessage) {
        val didShow = remoteMessagingRepository.didShow(remoteMessage.id)
        if (!didShow) {
            pixel.fire(AppPixelName.REMOTE_MESSAGE_SHOWN_UNIQUE, remoteMessage.asPixelParams())
            remoteMessagingRepository.markAsShown(remoteMessage)
        }
    }

    suspend fun onMessageDismissed(remoteMessage: RemoteMessage) {
        pixel.fire(AppPixelName.REMOTE_MESSAGE_DISMISSED, remoteMessage.asPixelParams())
        withContext(dispatchers.io()) {
            remoteMessagingRepository.dismissMessage(remoteMessage.id)
        }
    }

    suspend fun onPrimaryActionClicked(remoteMessage: RemoteMessage): Action? {
        pixel.fire(AppPixelName.REMOTE_MESSAGE_PRIMARY_ACTION_CLICKED, remoteMessage.asPixelParams())
        withContext(dispatchers.io()) {
            remoteMessagingRepository.dismissMessage(remoteMessage.id)
        }
        return remoteMessage.content.getPrimaryAction()
    }

    suspend fun onSecondaryActionClicked(remoteMessage: RemoteMessage): Action? {
        pixel.fire(AppPixelName.REMOTE_MESSAGE_SECONDARY_ACTION_CLICKED, remoteMessage.asPixelParams())
        withContext(dispatchers.io()) {
            remoteMessagingRepository.dismissMessage(remoteMessage.id)
        }
        return remoteMessage.content.getSecondaryAction()
    }

    fun onActionClicked(remoteMessage: RemoteMessage): Action? {
        pixel.fire(AppPixelName.REMOTE_MESSAGE_ACTION_CLICKED, remoteMessage.asPixelParams())
        return remoteMessage.content.getAction()
    }

    private fun Content.getPrimaryAction(): Action? {
        return when (this) {
            is Content.BigSingleAction -> {
                this.primaryAction
            }
            is Content.BigTwoActions -> {
                this.primaryAction
            }
            else -> null
        }
    }

    private fun Content.getSecondaryAction(): Action? {
        return when (this) {
            is Content.BigTwoActions -> {
                this.secondaryAction
            }
            else -> null
        }
    }

    private fun Content.getAction(): Action? {
        return when (this) {
            is Content.PromoSingleAction -> {
                this.action
            }
            else -> null
        }
    }

    private fun RemoteMessage.asPixelParams(): Map<String, String> = mapOf(Pixel.PixelParameter.MESSAGE_SHOWN to this.id)
}
