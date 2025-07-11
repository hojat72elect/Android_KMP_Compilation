

package com.duckduckgo.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.CompoundButton
import com.duckduckgo.mobile.android.R
import com.google.android.material.materialswitch.MaterialSwitch

class DaxSwitch @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.daxSwitchStyle,
) : MaterialSwitch(
    ctx,
    attrs,
    defStyleAttr,
)

fun DaxSwitch.quietlySetIsChecked(
    newCheckedState: Boolean,
    changeListener: CompoundButton.OnCheckedChangeListener?,
) {
    setOnCheckedChangeListener(null)
    isChecked = newCheckedState
    setOnCheckedChangeListener(changeListener)
}
