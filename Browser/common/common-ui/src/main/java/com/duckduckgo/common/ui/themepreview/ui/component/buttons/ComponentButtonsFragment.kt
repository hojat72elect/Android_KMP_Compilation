

package com.duckduckgo.common.ui.themepreview.ui.component.buttons

import com.duckduckgo.common.ui.themepreview.ui.component.Component
import com.duckduckgo.common.ui.themepreview.ui.component.ComponentFragment

class ComponentButtonsFragment : ComponentFragment() {
    override fun getComponents(): List<Component> {
        return listOf(
            Component.BUTTON,
        )
    }
}
