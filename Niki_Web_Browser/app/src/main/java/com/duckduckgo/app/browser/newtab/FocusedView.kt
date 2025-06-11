

package com.duckduckgo.app.browser.newtab

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.databinding.ViewFocusedViewBinding
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ViewScope

@InjectWith(ViewScope::class)
class FocusedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    private val binding: ViewFocusedViewBinding by viewBinding()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnClickListener { }
    }
}
