

package com.duckduckgo.common.ui.view.button

import android.content.Context
import android.util.AttributeSet
import com.duckduckgo.mobile.android.R
import com.google.android.material.radiobutton.MaterialRadioButton

class RadioButton @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.radioButtonStyle,
) : MaterialRadioButton(
    ctx,
    attrs,
    defStyleAttr,
)
