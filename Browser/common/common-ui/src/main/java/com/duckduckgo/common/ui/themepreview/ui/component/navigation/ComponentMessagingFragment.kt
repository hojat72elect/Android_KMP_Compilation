

package com.duckduckgo.common.ui.themepreview.ui.component.navigation

import com.duckduckgo.common.ui.themepreview.ui.component.Component
import com.duckduckgo.common.ui.themepreview.ui.component.ComponentFragment

class ComponentMessagingFragment : ComponentFragment() {
    override fun getComponents(): List<Component> {
        return listOf(Component.REMOTE_MESSAGE, Component.INFO_PANEL, Component.SNACKBAR)
    }
}
