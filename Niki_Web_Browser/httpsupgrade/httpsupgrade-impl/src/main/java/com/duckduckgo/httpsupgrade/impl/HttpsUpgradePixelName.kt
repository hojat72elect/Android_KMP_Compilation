

package com.duckduckgo.httpsupgrade.impl

import com.duckduckgo.app.statistics.pixels.Pixel

internal enum class HttpsUpgradePixelName(override val pixelName: String) : Pixel.PixelName {
    CREATE_BLOOM_FILTER_ERROR("m_create_bloom_filter_error"),
}
