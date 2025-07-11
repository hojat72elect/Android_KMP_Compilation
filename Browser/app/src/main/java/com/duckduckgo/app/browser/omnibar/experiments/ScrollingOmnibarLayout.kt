

package com.duckduckgo.app.browser.omnibar.experiments

import android.content.Context
import android.util.AttributeSet
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.omnibar.OmnibarLayout
import com.duckduckgo.app.browser.omnibar.model.OmnibarPosition
import com.duckduckgo.di.scopes.FragmentScope
import dagger.android.support.AndroidSupportInjection

@InjectWith(FragmentScope::class)
class ScrollingOmnibarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : OmnibarLayout(context, attrs, defStyle) {

    init {
        val attr =
            context.theme.obtainStyledAttributes(attrs, R.styleable.LegacyOmnibarView, defStyle, 0)
        omnibarPosition =
            OmnibarPosition.entries[attr.getInt(R.styleable.LegacyOmnibarView_omnibarPosition, 0)]

        val layout = if (omnibarPosition == OmnibarPosition.BOTTOM) {
            R.layout.view_new_omnibar_bottom
        } else {
            R.layout.view_new_omnibar
        }
        inflate(context, layout, this)

        AndroidSupportInjection.inject(this)
    }
}
