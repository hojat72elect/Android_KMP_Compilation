

package com.duckduckgo.app.browser.customtabs

import com.duckduckgo.app.statistics.pixels.Pixel

enum class CustomTabPixelNames(override val pixelName: String) : Pixel.PixelName {
    CUSTOM_TABS_OPENED("m_custom_tabs_opened"),
    CUSTOM_TABS_PRIVACY_DASHBOARD_OPENED("m_custom_tabs_privacy_dashboard_opened"),
    CUSTOM_TABS_MENU_OPENED("m_custom_tabs_menu_opened"),
    CUSTOM_TABS_MENU_REFRESH("m_custom_tabs_menu_refresh"),
    CUSTOM_TABS_OPEN_IN_DDG("m_custom_tabs_open_in_ddg"),
    CUSTOM_TABS_MENU_DISABLE_PROTECTIONS_ALLOW_LIST_ADD("m_custom_tabs_menu_disable_protections_allow_list_add"),
    CUSTOM_TABS_MENU_DISABLE_PROTECTIONS_ALLOW_LIST_REMOVE("m_custom_tabs_menu_disable_protections_allow_list_remove"),
}
