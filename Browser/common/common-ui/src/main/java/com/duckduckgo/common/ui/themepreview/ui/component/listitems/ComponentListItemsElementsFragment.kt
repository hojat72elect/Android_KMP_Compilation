

package com.duckduckgo.common.ui.themepreview.ui.component.listitems

import com.duckduckgo.common.ui.themepreview.ui.component.Component
import com.duckduckgo.common.ui.themepreview.ui.component.Component.MENU_ITEM
import com.duckduckgo.common.ui.themepreview.ui.component.Component.POPUP_MENU_ITEM
import com.duckduckgo.common.ui.themepreview.ui.component.Component.SECTION_HEADER_LIST_ITEM
import com.duckduckgo.common.ui.themepreview.ui.component.Component.SETTINGS_LIST_ITEM
import com.duckduckgo.common.ui.themepreview.ui.component.Component.SINGLE_LINE_LIST_ITEM
import com.duckduckgo.common.ui.themepreview.ui.component.Component.TWO_LINE_LIST_ITEM
import com.duckduckgo.common.ui.themepreview.ui.component.ComponentFragment

class ComponentListItemsElementsFragment : ComponentFragment() {
    override fun getComponents(): List<Component> {
        return listOf(SECTION_HEADER_LIST_ITEM, SINGLE_LINE_LIST_ITEM, TWO_LINE_LIST_ITEM, SETTINGS_LIST_ITEM, MENU_ITEM, POPUP_MENU_ITEM)
    }
}
