

package com.duckduckgo.common.utils.extensions

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

fun PackageManager.safeGetApplicationIcon(packageName: String): Drawable? {
    return runCatching {
        getApplicationIcon(packageName)
    }.getOrNull()
}
