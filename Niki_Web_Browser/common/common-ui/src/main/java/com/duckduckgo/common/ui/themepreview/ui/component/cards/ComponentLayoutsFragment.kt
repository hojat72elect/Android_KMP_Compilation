

package com.duckduckgo.common.ui.themepreview.ui.component.cards

import com.duckduckgo.common.ui.themepreview.ui.component.Component
import com.duckduckgo.common.ui.themepreview.ui.component.Component.EXPANDABLE_LAYOUT
import com.duckduckgo.common.ui.themepreview.ui.component.ComponentFragment

class ComponentLayoutsFragment : ComponentFragment() {
    override fun getComponents(): List<Component> {
        return listOf(EXPANDABLE_LAYOUT, Component.CARD)
    }
}
