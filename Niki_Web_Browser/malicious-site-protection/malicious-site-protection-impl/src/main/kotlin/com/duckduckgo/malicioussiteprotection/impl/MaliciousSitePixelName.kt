

package com.duckduckgo.malicioussiteprotection.impl

import com.duckduckgo.app.statistics.pixels.Pixel

enum class MaliciousSitePixelName(override val pixelName: String) : Pixel.PixelName {
    MALICIOUS_SITE_CLIENT_TIMEOUT("m_malicious-site-protection_client-timeout"),
}
