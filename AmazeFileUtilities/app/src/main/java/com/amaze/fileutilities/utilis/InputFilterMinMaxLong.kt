package com.amaze.fileutilities.utilis

import android.text.InputFilter
import android.text.Spanned

class InputFilterMinMaxLong(
    private val min: Long,
    private val max: Long
) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (dest == null || source == null) {
            return ""
        }
        return try {
            val input =
                "${dest.subSequence(0, dstart)}$source${dest.subSequence(dend, dest.length)}"
            val number = input.toInt()
            if (number < min) {
                ""
            } else if (number > max) {
                ""
            } else {
                null
            }
        } catch (e: NumberFormatException) {
            ""
        }
    }
}
