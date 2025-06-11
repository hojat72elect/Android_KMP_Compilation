

package com.duckduckgo.common.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView

class Chip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun setChipText(@StringRes textResource: Int) {
        setText(textResource)
    }

    fun setChipIcon(@DrawableRes iconResource: Int) {
        leftDrawable(iconResource)
    }
}
