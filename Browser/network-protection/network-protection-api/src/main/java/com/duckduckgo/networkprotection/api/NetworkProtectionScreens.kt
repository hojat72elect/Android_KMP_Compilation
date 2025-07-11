

package com.duckduckgo.networkprotection.api

import com.duckduckgo.navigation.api.GlobalActivityStarter.ActivityParams
sealed class NetworkProtectionScreens {
    /**
     * Use this model to launch the NetworkProtectionManagement screen
     */
    object NetworkProtectionManagementScreenNoParams : ActivityParams

    /**
     * Use this model to launch the NetworkProtectionManagement screen
     * @param enable `true` if you want to both open the screen and enable NetP in case it's not yet enabled.
     */
    data class NetworkProtectionManagementScreenAndEnable(val enable: Boolean) : ActivityParams

    /**
     * Use this model to launch the NetP app exclusion list screen
     */
    object NetPAppExclusionListNoParams : ActivityParams
}
