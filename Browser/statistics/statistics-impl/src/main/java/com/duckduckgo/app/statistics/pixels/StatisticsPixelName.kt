

package com.duckduckgo.app.statistics.pixels

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PACKAGE_PRIVATE
import com.duckduckgo.app.statistics.pixels.Pixel.PixelName

@VisibleForTesting(otherwise = PACKAGE_PRIVATE)
enum class StatisticsPixelName(override val pixelName: String) : PixelName {
    BROWSER_DAILY_ACTIVE_FEATURE_STATE("m_browser_feature_daily_active_user_d"),
    RETENTION_SEGMENTS("m_retention_segments"),
}
