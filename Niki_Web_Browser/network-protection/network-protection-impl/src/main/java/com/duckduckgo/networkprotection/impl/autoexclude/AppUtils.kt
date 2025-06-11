

package com.duckduckgo.networkprotection.impl.autoexclude

import android.content.pm.PackageManager

fun PackageManager.getAppName(packageName: String): String {
    return getApplicationInfo(packageName, 0).run {
        getApplicationLabel(this)
    }.toString()
}
