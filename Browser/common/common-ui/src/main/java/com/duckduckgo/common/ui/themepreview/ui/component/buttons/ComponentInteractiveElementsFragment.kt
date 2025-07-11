

package com.duckduckgo.common.ui.themepreview.ui.component.buttons

import com.duckduckgo.common.ui.themepreview.ui.component.Component
import com.duckduckgo.common.ui.themepreview.ui.component.ComponentFragment

class ComponentInteractiveElementsFragment : ComponentFragment() {
    override fun getComponents(): List<Component> {
        return listOf(
            Component.SWITCH,
            Component.RADIO_BUTTON,
            Component.CHECKBOX,
            Component.SLIDER,
            Component.SEARCH_BAR,
        )
    }
}
