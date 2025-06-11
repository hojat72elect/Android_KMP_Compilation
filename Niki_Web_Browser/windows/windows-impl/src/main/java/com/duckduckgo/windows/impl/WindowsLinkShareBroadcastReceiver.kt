

package com.duckduckgo.windows.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.ReceiverScope
import dagger.android.AndroidInjection
import javax.inject.Inject

@InjectWith(ReceiverScope::class)
class WindowsLinkShareBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var pixel: Pixel

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        pixel.fire(WindowsPixelNames.WINDOWS_WAITLIST_SHARE_SHARED)
    }
}
