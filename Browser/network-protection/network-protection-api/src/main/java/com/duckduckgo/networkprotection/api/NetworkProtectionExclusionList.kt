

package com.duckduckgo.networkprotection.api

interface NetworkProtectionExclusionList {
    /**
     * This method returns if the specified app is excluded from NetworkProtection
     *
     * @param packageName - package name of the app to be checked from NetP exclusion list
     */
    suspend fun isExcluded(packageName: String): Boolean
}
