

package com.duckduckgo.common.ui.view.divider

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.mobile.android.R
import com.duckduckgo.mobile.android.databinding.ViewVerticalDividerBinding

class VerticalDivider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewVerticalDividerBinding by viewBinding()

    init {
        val typedArray =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.VerticalDivider,
                0,
                R.style.Widget_DuckDuckGo_VerticalDivider,
            )

        val defaultPadding = typedArray.getBoolean(R.styleable.VerticalDivider_defaultPadding, false)

        val sidePadding = if (defaultPadding) {
            resources.getDimensionPixelOffset(R.dimen.verticalDividerSidePadding)
        } else {
            0
        }

        binding.root.setPadding(sidePadding, 0, sidePadding, 0)

        typedArray.recycle()
    }
}
