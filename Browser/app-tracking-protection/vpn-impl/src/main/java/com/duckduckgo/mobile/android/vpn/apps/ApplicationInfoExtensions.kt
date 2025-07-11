

package com.duckduckgo.mobile.android.vpn.apps

import android.content.pm.ApplicationInfo
import com.duckduckgo.mobile.android.vpn.exclusion.AppCategory

private fun parseAppCategory(category: Int): AppCategory {
    return when (category) {
        ApplicationInfo.CATEGORY_AUDIO -> AppCategory.Audio
        ApplicationInfo.CATEGORY_VIDEO -> AppCategory.Video
        ApplicationInfo.CATEGORY_GAME -> AppCategory.Game
        ApplicationInfo.CATEGORY_IMAGE -> AppCategory.Image
        ApplicationInfo.CATEGORY_SOCIAL -> AppCategory.Social
        ApplicationInfo.CATEGORY_NEWS -> AppCategory.News
        ApplicationInfo.CATEGORY_MAPS -> AppCategory.Maps
        ApplicationInfo.CATEGORY_PRODUCTIVITY -> AppCategory.Productivity
        else -> AppCategory.Undefined
    }
}

fun ApplicationInfo.parseAppCategory(): AppCategory {
    return parseAppCategory(category)
}

fun ApplicationInfo.isGame(): Boolean {
    return category == ApplicationInfo.CATEGORY_GAME
}

fun ApplicationInfo.isSystemApp(): Boolean {
    return (flags and ApplicationInfo.FLAG_SYSTEM) != 0
}

fun ApplicationInfo.getAppType(): String? {
    return if ((flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
        "System"
    } else {
        null
    }
}
