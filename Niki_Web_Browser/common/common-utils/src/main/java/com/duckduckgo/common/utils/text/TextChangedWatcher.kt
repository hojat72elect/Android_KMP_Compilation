

package com.duckduckgo.common.utils.text

import android.text.Editable
import android.text.TextWatcher

abstract class TextChangedWatcher : TextWatcher {

    abstract override fun afterTextChanged(editable: Editable)

    override fun beforeTextChanged(
        p0: CharSequence,
        p1: Int,
        p2: Int,
        p3: Int,
    ) {
    }

    override fun onTextChanged(
        charSequence: CharSequence,
        start: Int,
        before: Int,
        count: Int,
    ) {
    }
}
