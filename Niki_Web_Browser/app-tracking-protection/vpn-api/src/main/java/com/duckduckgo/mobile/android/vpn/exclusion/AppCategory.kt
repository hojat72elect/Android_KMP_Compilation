

package com.duckduckgo.mobile.android.vpn.exclusion

sealed class AppCategory {
    object Undefined : AppCategory() {
        override fun toString() = "CATEGORY_UNDEFINED"
    }

    object Game : AppCategory() {
        override fun toString() = "CATEGORY_GAME"
    }

    object Audio : AppCategory() {
        override fun toString() = "CATEGORY_AUDIO"
    }

    object Video : AppCategory() {
        override fun toString() = "CATEGORY_VIDEO"
    }

    object Image : AppCategory() {
        override fun toString() = "CATEGORY_IMAGE"
    }

    object Social : AppCategory() {
        override fun toString() = "CATEGORY_SOCIAL"
    }

    object News : AppCategory() {
        override fun toString() = "CATEGORY_NEWS"
    }

    object Maps : AppCategory() {
        override fun toString() = "CATEGORY_MAPS"
    }

    object Productivity : AppCategory() {
        override fun toString() = "CATEGORY_PRODUCTIVITY"
    }
}
