

package com.duckduckgo.common.ui.themepreview.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.duckduckgo.common.ui.themepreview.ui.component.ComponentOtherFragment
import com.duckduckgo.common.ui.themepreview.ui.component.buttons.ComponentButtonsFragment
import com.duckduckgo.common.ui.themepreview.ui.component.buttons.ComponentInteractiveElementsFragment
import com.duckduckgo.common.ui.themepreview.ui.component.cards.ComponentLayoutsFragment
import com.duckduckgo.common.ui.themepreview.ui.component.listitems.ComponentListItemsElementsFragment
import com.duckduckgo.common.ui.themepreview.ui.component.navigation.ComponentMessagingFragment
import com.duckduckgo.common.ui.themepreview.ui.component.textinput.ComponentTextInputFragment
import com.duckduckgo.common.ui.themepreview.ui.dialogs.DialogsFragment
import com.duckduckgo.common.ui.themepreview.ui.palette.ColorPaletteFragment
import com.duckduckgo.common.ui.themepreview.ui.typography.TypographyFragment
import com.duckduckgo.mobile.android.R

/** View pager to show all tabbed destinations - Instructions, Theme Summary and Components. */
class AppComponentsPagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    enum class MainFragments(val titleRes: Int) {
        PALETTE(R.string.tab_title_color_palette),
        TYPOGRAPHY(R.string.tab_title_typography),
        BUTTONS(R.string.tab_title_buttons),
        TEXT_INPUT(R.string.tab_title_text_input),
        DIALOGS(R.string.tab_title_dialogs),
        LAYOUTS(R.string.tab_title_layouts),
        INTERACTIVE_ELEMENTS(R.string.tab_title_component_interactive),
        MESSAGING(R.string.tab_title_component_messaging),
        LIST_ITEMS(R.string.tab_title_component_list_items),
        OTHERS(R.string.tab_title_component_others),
    }

    override fun getCount(): Int = MainFragments.values().size

    private fun getItemType(position: Int): MainFragments {
        return MainFragments.values()[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(getItemType(position).titleRes)
    }

    override fun getItem(position: Int): Fragment {
        return when (getItemType(position)) {
            MainFragments.PALETTE -> ColorPaletteFragment()
            MainFragments.TYPOGRAPHY -> TypographyFragment()
            MainFragments.BUTTONS -> ComponentButtonsFragment()
            MainFragments.TEXT_INPUT -> ComponentTextInputFragment()
            MainFragments.DIALOGS -> DialogsFragment()
            MainFragments.LAYOUTS -> ComponentLayoutsFragment()
            MainFragments.INTERACTIVE_ELEMENTS -> ComponentInteractiveElementsFragment()
            MainFragments.MESSAGING -> ComponentMessagingFragment()
            MainFragments.LIST_ITEMS -> ComponentListItemsElementsFragment()
            MainFragments.OTHERS -> ComponentOtherFragment()
        }
    }
}
