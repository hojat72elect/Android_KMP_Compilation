

package com.duckduckgo.mobile.android.vpn.exclusion

interface AppCategoryDetector {
    /**
     * Maps the app's package name into [AppCategory]
     */
    fun getAppCategory(packageName: String): AppCategory
}
