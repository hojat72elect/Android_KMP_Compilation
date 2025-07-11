package com.duckduckgo.app.anr

import com.duckduckgo.app.statistics.pixels.Pixel.PixelName

internal enum class CrashPixel(override val pixelName: String) : PixelName {
    APPLICATION_CRASH_GLOBAL("m_d_ac_g"),
    APPLICATION_CRASH_GLOBAL_VERIFIED_INSTALL("m_app_crashed_on_verified_play_store_install"),
}
