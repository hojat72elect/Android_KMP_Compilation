

package com.duckduckgo.macos.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.ReceiverScope
import com.duckduckgo.macos.impl.MacOsPixelNames.MACOS_WAITLIST_SHARE_SHARED
import dagger.android.AndroidInjection
import javax.inject.Inject

@InjectWith(ReceiverScope::class)
class MacOsLinkShareBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var pixel: Pixel

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        pixel.fire(MACOS_WAITLIST_SHARE_SHARED)
    }
}
