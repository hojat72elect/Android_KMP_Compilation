

package com.duckduckgo.app.browser.addtohome

import android.content.Context
import androidx.core.content.pm.ShortcutManagerCompat
import javax.inject.Inject
import timber.log.Timber

interface AddToHomeCapabilityDetector {
    fun isAddToHomeSupported(): Boolean
}

class AddToHomeSystemCapabilityDetector @Inject constructor(val context: Context) : AddToHomeCapabilityDetector {

    override fun isAddToHomeSupported(): Boolean {
        val supported = ShortcutManagerCompat.isRequestPinShortcutSupported(context)
        Timber.v("Add to home is %ssupported", if (supported) "" else "not ")
        return supported
    }
}
