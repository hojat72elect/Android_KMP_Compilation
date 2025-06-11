

package com.duckduckgo.site.permissions.impl

import com.duckduckgo.app.statistics.pixels.Pixel

enum class SitePermissionsPixelName(override val pixelName: String) : Pixel.PixelName {
    PERMISSION_DIALOG_IMPRESSION("m_site_permissions_dialog_impresssion"),
    PERMISSION_DIALOG_CLICK("m_site_permissions_dialog_click"),
}

object SitePermissionsPixelParameters {
    const val PERMISSION_TYPE = "type"
    const val PERMISSION_SELECTION = "selection"
}

object SitePermissionsPixelValues {
    const val LOCATION = "location"
    const val CAMERA = "camera"
    const val MICROPHONE = "microphone"
    const val CAMERA_AND_MICROPHONE = "camera_and_microphone"
    const val DRM = "drm"
    const val ALLOW_ALWAYS = "allow_always"
    const val ALLOW_ONCE = "allow_once"
    const val DENY_ALWAYS = "deny_always"
    const val DENY_ONCE = "deny_once"
}
