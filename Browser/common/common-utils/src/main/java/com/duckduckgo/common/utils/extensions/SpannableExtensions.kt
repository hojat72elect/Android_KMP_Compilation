

package com.duckduckgo.common.utils.extensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan

fun String.applyBoldSpanTo(textToStyle: List<String>): SpannableStringBuilder {
    val spannable = SpannableStringBuilder(this)
    textToStyle.forEach {
        val index = this.indexOf(it)
        spannable.setSpan(StyleSpan(Typeface.BOLD), index, index + it.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
    return spannable
}

fun String.applyBoldSpanTo(textToStyle: String = this): SpannableStringBuilder {
    val spannable = SpannableStringBuilder(this)
    val index = this.indexOf(textToStyle)
    spannable.setSpan(StyleSpan(Typeface.BOLD), index, index + textToStyle.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    return spannable
}

fun String.applyUnderscoreSpanTo(textToStyle: List<String>): SpannableStringBuilder {
    val spannable = SpannableStringBuilder(this)
    textToStyle.forEach {
        val index = this.indexOf(it)
        spannable.setSpan(UnderlineSpan(), index, index + it.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
    return spannable
}
