

package com.duckduckgo.macos.impl

import com.duckduckgo.app.statistics.pixels.Pixel

enum class MacOsPixelNames(override val pixelName: String) : Pixel.PixelName {
    MACOS_WAITLIST_SHARE_PRESSED("m_macos_waitlist_did_press_share_button"),
    MACOS_WAITLIST_SHARE_SHARED("m_macos_waitlist_did_press_share_button_shared"),
}
