

package com.duckduckgo.mobile.android.app.tracking.ui

import com.duckduckgo.navigation.api.GlobalActivityStarter

sealed class AppTrackingProtectionScreens {
    /**
     * Use this class to launch the AppTP Tracker Activity Screen
     * ```kotlin
     * globalActivityStarter.start(context, DeviceShieldTrackerActivityWithEmptyParams)
     * ```
     */
    object AppTrackerActivityWithEmptyParams : GlobalActivityStarter.ActivityParams

    /**
     * Use this class to launch the AppTP onboarding screen
     * ```kotlin
     * globalActivityStarter.start(context, AppTrackerOnboardingActivityWithEmptyParamsParams)
     * ```
     */
    object AppTrackerOnboardingActivityWithEmptyParamsParams : GlobalActivityStarter.ActivityParams
}
