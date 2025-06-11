

package com.duckduckgo.sync.impl.ui.setup

interface SetupFlowListener {
    fun launchRecoveryCodeScreen()
    fun launchCreateAccountScreen()
    fun launchRecoverAccountScreen()
    fun launchDeviceConnectedScreen()
    fun launchGetAppOnOtherPlatformsScreen()
    fun finishSetup()
}
