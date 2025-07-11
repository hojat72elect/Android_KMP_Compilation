

package com.duckduckgo.app.browser.tabswitcher

import android.content.Context
import android.util.AttributeSet
import com.duckduckgo.app.browser.databinding.ViewExperimentalTabSwitcherButtonBinding
import com.duckduckgo.common.ui.view.gone
import com.duckduckgo.common.ui.view.show
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.mobile.android.R as CommonR

class ExperimentalTabSwitcherButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : TabSwitcherButton(context, attrs, defStyleAttr) {

    private val binding: ViewExperimentalTabSwitcherButtonBinding by viewBinding()

    override var hasUnread: Boolean = false
        set(value) {
            if (field != value) {
                if (value) {
                    binding.tabsImageView.setImageResource(CommonR.drawable.ic_tab_24_highlighted)
                } else {
                    binding.tabsImageView.setImageResource(CommonR.drawable.ic_tab_24)
                }
            }
            field = value
        }

    override var count = 0
        set(value) {
            if (field != value) {
                if (value < 100) {
                    binding.tabCount.text = "$value"
                    binding.tabCount.show()
                    binding.tabCountInfinite.gone()
                } else {
                    binding.tabCount.gone()
                    binding.tabCountInfinite.show()
                }
            }
            field = value
        }
}
