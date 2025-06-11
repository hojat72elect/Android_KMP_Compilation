

package com.duckduckgo.mobile.android.vpn.exclusion

interface SystemAppOverridesProvider {
    /**
     * This is method returns system apps that we want to be included in the exclusion list.
     *
     * @return a list of app packages that is part of the system app overrides list
     */
    fun getSystemAppOverridesList(): List<String>
}
