package ca.hojat.smart.gallery.shared.helpers

import androidx.core.net.toUri

class MyContentProvider {
    companion object {
        private const val AUTHORITY = "ca.hojat.smart.gallery.provider"
        val MY_CONTENT_URI = "content://$AUTHORITY/themes".toUri()


        const val COL_TEXT_COLOR = "text_color"
        const val COL_BACKGROUND_COLOR = "background_color"
        const val COL_PRIMARY_COLOR = "primary_color"
        const val COL_ACCENT_COLOR = "accent_color"
        const val COL_APP_ICON_COLOR = "app_icon_color"
        const val COL_LAST_UPDATED_TS = "last_updated_ts"
    }
}
