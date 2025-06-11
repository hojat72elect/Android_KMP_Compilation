

package com.duckduckgo.common.utils.extensions

import android.content.Intent
import android.os.Build
import java.io.Serializable

inline fun <reified T : Serializable> Intent.getSerializableExtra(name: String): T? =
    if (Build.VERSION.SDK_INT >= 33) {
        getSerializableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(name) as? T
    }
