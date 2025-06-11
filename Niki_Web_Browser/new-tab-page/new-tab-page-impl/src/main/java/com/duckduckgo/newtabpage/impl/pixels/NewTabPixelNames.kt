

package com.duckduckgo.newtabpage.impl.pixels

import com.duckduckgo.app.statistics.pixels.Pixel

enum class NewTabPixelNames(override val pixelName: String) : Pixel.PixelName {
    CUSTOMIZE_PAGE_PRESSED("m_new_tab_page_customize"),
    SHORTCUT_PRESSED("m_new_tab_page_shortcut_clicked_"),
    SHORTCUT_REMOVED("m_new_tab_page_customize_shortcut_removed_"),
    SHORTCUT_ADDED("m_new_tab_page_customize_shortcut_added_"),
    SHORTCUT_SECTION_TOGGLED_OFF("m_new_tab_page_customize_section_off_shortcuts"),
    SHORTCUT_SECTION_TOGGLED_ON("m_new_tab_page_customize_section_on_shortcuts"),
    SECTION_REARRANGED("m_new_tab_page_customize_section_reordered"),
    NEW_TAB_DISPLAYED("m_new_tab_page_displayed"),
    NEW_TAB_DISPLAYED_UNIQUE("m_new_tab_page_displayed_unique"),
}

object NewTabPixelParameters {
    const val SHORTCUTS = "shortcuts"
    const val FAVORITES = "favorites"
    const val APP_TRACKING_PROTECTION = "appTP"
    const val FAVORITES_COUNT = "favoriteCount"
}

object NewTabPixelValues {
    const val SECTION_ENABLED = "1"
    const val SECTION_DISABLED = "0"
    const val FAVORITES_2_3 = "2_3"
    const val FAVORITES_4_5 = "4_5"
    const val FAVORITES_6_10 = "6_10"
    const val FAVORITES_11_15 = "11_15"
    const val FAVORITES_16_25 = "16_25"
    const val FAVORITES_25 = ">25"
}
