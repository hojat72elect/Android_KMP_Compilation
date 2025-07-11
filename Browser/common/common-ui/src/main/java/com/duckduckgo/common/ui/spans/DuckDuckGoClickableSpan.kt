

package com.duckduckgo.common.ui.spans

import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class DuckDuckGoClickableSpan(val isUnderline: Boolean = false) : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = isUnderline
    }
}
