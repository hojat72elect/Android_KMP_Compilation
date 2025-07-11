

package com.duckduckgo.privacyprotectionspopup.impl

import com.duckduckgo.app.statistics.pixels.Pixel.PixelName
import com.duckduckgo.app.statistics.pixels.Pixel.PixelType

enum class PrivacyProtectionsPopupPixelName(
    override val pixelName: String,
    val type: PixelType,
) : PixelName
