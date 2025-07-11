

package com.duckduckgo.remote.messaging.impl

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.Action
import com.duckduckgo.remote.messaging.api.Content
import com.duckduckgo.remote.messaging.api.RemoteMessage
import com.duckduckgo.remote.messaging.api.RemoteMessageModel
import com.duckduckgo.remote.messaging.api.RemoteMessagingRepository
import com.duckduckgo.remote.messaging.impl.pixels.RemoteMessagingPixels
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

@ContributesBinding(AppScope::class)
class RealRemoteMessageModel @Inject constructor(
    private val remoteMessagingRepository: RemoteMessagingRepository,
    private val remoteMessagingPixels: RemoteMessagingPixels,
    private val dispatchers: DispatcherProvider,
) : RemoteMessageModel {

    override fun getActiveMessage(): RemoteMessage? = remoteMessagingRepository.message()
    override fun getActiveMessages() = remoteMessagingRepository.messageFlow()

    override suspend fun onMessageShown(remoteMessage: RemoteMessage) {
        withContext(dispatchers.io()) {
            remoteMessagingPixels.fireRemoteMessageShownPixel(remoteMessage)
            remoteMessagingRepository.markAsShown(remoteMessage)
        }
    }

    override suspend fun onMessageDismissed(remoteMessage: RemoteMessage) {
        remoteMessagingPixels.fireRemoteMessageDismissedPixel(remoteMessage)
        withContext(dispatchers.io()) {
            remoteMessagingRepository.dismissMessage(remoteMessage.id)
        }
    }

    override suspend fun onPrimaryActionClicked(remoteMessage: RemoteMessage): Action? {
        remoteMessagingPixels.fireRemoteMessagePrimaryActionClickedPixel(remoteMessage)
        withContext(dispatchers.io()) {
            remoteMessagingRepository.dismissMessage(remoteMessage.id)
        }
        return remoteMessage.content.getPrimaryAction()
    }

    override suspend fun onSecondaryActionClicked(remoteMessage: RemoteMessage): Action? {
        remoteMessagingPixels.fireRemoteMessageSecondaryActionClickedPixel(remoteMessage)
        withContext(dispatchers.io()) {
            remoteMessagingRepository.dismissMessage(remoteMessage.id)
        }
        return remoteMessage.content.getSecondaryAction()
    }

    override suspend fun onActionClicked(remoteMessage: RemoteMessage): Action? {
        remoteMessagingPixels.fireRemoteMessageActionClickedPixel(remoteMessage)
        val action = remoteMessage.content.getAction()
        if (action !is Action.Share) {
            withContext(dispatchers.io()) {
                remoteMessagingRepository.dismissMessage(remoteMessage.id)
            }
        }
        return action
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
}
