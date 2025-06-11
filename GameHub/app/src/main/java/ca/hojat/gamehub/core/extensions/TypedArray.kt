@file:JvmName("TypedArrayUtils")

package ca.hojat.gamehub.core.extensions

import android.content.res.TypedArray
import androidx.annotation.StyleableRes

fun TypedArray.getString(@StyleableRes index: Int, default: CharSequence = ""): CharSequence {
    return (getString(index) ?: default)
}

