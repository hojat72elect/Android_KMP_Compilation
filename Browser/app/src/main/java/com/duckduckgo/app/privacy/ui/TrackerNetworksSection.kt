

package com.duckduckgo.app.privacy.ui

import androidx.annotation.StringRes
import com.duckduckgo.app.trackerdetection.model.TrackerStatus

data class TrackerNetworksSection(
    @StringRes val descriptionRes: Int? = null,
    @StringRes val linkTextRes: Int? = null,
    @StringRes val linkUrlRes: Int? = null,
    val trackerStatus: TrackerStatus,
    val domain: String? = null,
)
