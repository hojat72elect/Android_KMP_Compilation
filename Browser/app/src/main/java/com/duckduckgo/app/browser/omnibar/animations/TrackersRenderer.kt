

package com.duckduckgo.app.browser.omnibar.animations

import android.content.Context
import androidx.annotation.DrawableRes
import java.util.*

class TrackersRenderer {
    @DrawableRes
    fun networkLogoIcon(
        context: Context,
        networkName: String,
    ): Int? {
        return networkIcon(context, networkName, "network_logo_")
    }

    private fun networkIcon(
        context: Context,
        networkName: String,
        prefix: String,
    ): Int? {
        val drawable = "$prefix$networkName"
            .replace(" ", "_")
            .replace(".", "")
            .replace(",", "")
            .lowercase(Locale.ROOT)
        val resource = context.resources.getIdentifier(drawable, "drawable", context.packageName)
        return if (resource != 0) resource else null
    }
}
