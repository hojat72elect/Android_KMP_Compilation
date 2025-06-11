package com.duckduckgo.autoconsent.impl.pixels

import com.duckduckgo.app.statistics.pixels.Pixel

enum class AutoConsentPixel(override val pixelName: String) : Pixel.PixelName {

    SETTINGS_AUTOCONSENT_SHOWN("m_settings_autoconsent_shown"),
    SETTINGS_AUTOCONSENT_ON("m_settings_autoconsent_on"),
    SETTINGS_AUTOCONSENT_OFF("m_settings_autoconsent_off"),
}
