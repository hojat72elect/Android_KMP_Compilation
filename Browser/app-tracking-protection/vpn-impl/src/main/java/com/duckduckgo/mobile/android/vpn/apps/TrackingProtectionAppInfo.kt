

package com.duckduckgo.mobile.android.vpn.apps

import com.duckduckgo.mobile.android.vpn.exclusion.AppCategory

data class TrackingProtectionAppInfo(
    val packageName: String,
    val name: String,
    val category: AppCategory = AppCategory.Undefined,
    val isExcluded: Boolean = false,
    val knownProblem: Int,
    val userModified: Boolean,
) {
    companion object {
        const val NO_ISSUES = 0
        const val KNOWN_ISSUES_EXCLUSION_REASON = 1
        const val LOADS_WEBSITES_EXCLUSION_REASON = 2
        const val EXCLUDED_THROUGH_NETP = 3
    }

    fun isProblematic(): Boolean {
        return knownProblem > 0
    }
}
