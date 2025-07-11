

package com.duckduckgo.common.utils.extensions

import android.widget.EditText
import com.duckduckgo.common.utils.text.TextChangedWatcher

fun EditText.isDifferent(newInput: String?): Boolean = text.toString() != newInput

fun EditText.updateIfDifferent(newInput: String) {
    if (isDifferent(newInput)) {
        setText(newInput)
    }
}

fun EditText.replaceTextChangedListener(textWatcher: TextChangedWatcher) {
    removeTextChangedListener(textWatcher)
    addTextChangedListener(textWatcher)
}
