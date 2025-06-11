

package com.duckduckgo.windows.impl

import com.duckduckgo.app.statistics.pixels.Pixel

enum class WindowsPixelNames(override val pixelName: String) : Pixel.PixelName {
    WINDOWS_WAITLIST_SHARE_PRESSED("m_windows_waitlist_did_press_share_button"),
    WINDOWS_WAITLIST_SHARE_SHARED("m_windows_waitlist_did_press_share_button_shared"),
}
