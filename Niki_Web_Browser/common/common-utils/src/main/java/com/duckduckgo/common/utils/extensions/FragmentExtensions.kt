

package com.duckduckgo.common.utils.extensions

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.io.Serializable

fun Fragment.showKeyboard(editText: EditText) = activity?.showKeyboard(editText)

fun Fragment.hideKeyboard(editText: EditText) = activity?.hideKeyboard(editText)

inline fun <reified T : Serializable> Bundle.getSerializable(name: String): T? =
    if (Build.VERSION.SDK_INT >= 33) {
        getSerializable(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializable(name) as? T
    }
