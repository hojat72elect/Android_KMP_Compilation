

package com.duckduckgo.app.browser.tabswitcher

import android.content.Context
import android.util.AttributeSet
import com.duckduckgo.app.browser.databinding.ViewTabSwitcherButtonBinding
import com.duckduckgo.common.ui.viewbinding.viewBinding

class ProductionTabSwitcherButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : TabSwitcherButton(context, attrs, defStyleAttr) {

    private val binding: ViewTabSwitcherButtonBinding by viewBinding()

    override var count = 0
        set(value) {
            field = value
            val text = if (count < 100) "$count" else "~"
            binding.tabCount.text = text
        }
}
