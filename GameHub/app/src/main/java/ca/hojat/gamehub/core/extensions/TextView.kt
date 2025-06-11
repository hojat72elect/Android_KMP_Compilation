@file:JvmName("TextViewUtils")

package ca.hojat.gamehub.core.extensions

import android.graphics.drawable.Drawable
import android.widget.TextView

private const val COMPOUND_DRAWABLE_INDEX_LEFT = 0
private const val COMPOUND_DRAWABLE_INDEX_TOP = 1
private const val COMPOUND_DRAWABLE_INDEX_RIGHT = 2
private const val COMPOUND_DRAWABLE_INDEX_BOTTOM = 3

var TextView.startDrawable: Drawable?
    set(value) = updateCompoundDrawable(start = value)
    get() = compoundDrawablesRelative[COMPOUND_DRAWABLE_INDEX_LEFT]

var TextView.topDrawable: Drawable?
    set(value) = updateCompoundDrawable(top = value)
    get() = compoundDrawablesRelative[COMPOUND_DRAWABLE_INDEX_TOP]

var TextView.endDrawable: Drawable?
    set(value) = updateCompoundDrawable(end = value)
    get() = compoundDrawablesRelative[COMPOUND_DRAWABLE_INDEX_RIGHT]

var TextView.bottomDrawable: Drawable?
    set(value) = updateCompoundDrawable(bottom = value)
    get() = compoundDrawablesRelative[COMPOUND_DRAWABLE_INDEX_BOTTOM]

fun TextView.updateCompoundDrawable(
    start: Drawable? = startDrawable,
    top: Drawable? = topDrawable,
    end: Drawable? = endDrawable,
    bottom: Drawable? = bottomDrawable
) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
}

