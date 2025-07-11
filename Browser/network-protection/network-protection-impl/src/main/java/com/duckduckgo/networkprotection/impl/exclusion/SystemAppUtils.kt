

package com.duckduckgo.networkprotection.impl.exclusion

import android.content.pm.ApplicationInfo

internal fun ApplicationInfo.isSystemApp(): Boolean {
    return (flags and ApplicationInfo.FLAG_SYSTEM) != 0
}
