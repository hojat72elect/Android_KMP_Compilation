

package com.duckduckgo.remote.messaging.impl.newtab

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ReceiverScope
import com.duckduckgo.remote.messaging.api.RemoteMessagingRepository
import com.duckduckgo.remote.messaging.impl.pixels.RemoteMessagingPixels
import dagger.android.AndroidInjection
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@InjectWith(ReceiverScope::class)
class SharePromoLinkBroadCastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var remoteMessagingRepository: RemoteMessagingRepository

    @Inject
    @AppCoroutineScope
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    @Inject
    lateinit var pixels: RemoteMessagingPixels

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        AndroidInjection.inject(this, context)
        onPromoLinkSharedSuccessfully()
    }

    private fun onPromoLinkSharedSuccessfully() {
        coroutineScope.launch(dispatcherProvider.io()) {
            remoteMessagingRepository.messageFlow().map { it?.id }.take(1).collect {
                val messageId = it.orEmpty()
                remoteMessagingRepository.dismissMessage(messageId)
                val pixelsParams: Map<String, String> = mapOf(
                    Pixel.PixelParameter.MESSAGE_SHOWN to messageId,
                    Pixel.PixelParameter.ACTION_SUCCESS to "true",
                )

                pixels.fireRemoteMessageSharedPixel(pixelsParams)
            }
        }
    }
}
