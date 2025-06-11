

package com.duckduckgo.app.generalsettings.showonapplaunch.model

sealed class ShowOnAppLaunchOption(val id: Int) {

    data object LastOpenedTab : ShowOnAppLaunchOption(1)
    data object NewTabPage : ShowOnAppLaunchOption(2)
    data class SpecificPage(val url: String, val resolvedUrl: String? = null) : ShowOnAppLaunchOption(3)

    companion object {

        fun mapToOption(id: Int): ShowOnAppLaunchOption = when (id) {
            1 -> LastOpenedTab
            2 -> NewTabPage
            3 -> SpecificPage("")
            else -> throw IllegalArgumentException("Unknown id: $id")
        }

        fun getDailyPixelValue(option: ShowOnAppLaunchOption) = when (option) {
            LastOpenedTab -> "last_opened_tab"
            NewTabPage -> "new_tab_page"
            is SpecificPage -> "specific_page"
        }
    }
}
