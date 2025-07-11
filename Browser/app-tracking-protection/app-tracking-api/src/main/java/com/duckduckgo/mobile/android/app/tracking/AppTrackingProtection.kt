

package com.duckduckgo.mobile.android.app.tracking

interface AppTrackingProtection {

    /**
     * This method returns whether the user has gone through AppTP onboarding
     * @return Returns `true` if user has gone through AppTP onboarding, `false` otherwise
     */
    suspend fun isOnboarded(): Boolean

    /**
     * This is a suspend function because the operation can take time.
     * You DO NOT need to set any dispatcher to call this suspend function
     * @return `true` when NetP is enabled
     */
    suspend fun isEnabled(): Boolean

    /**
     * This is a suspend function because the operation can take time
     * You DO NOT need to set any dispatcher to call this suspend function
     * @return `true` when NetP is enabled AND the VPN is running, `false` otherwise
     */
    suspend fun isRunning(): Boolean

    /**
     * This method will restart the App Tracking Protection feature by disabling it and re-enabling back again
     */
    fun restart()

    /**
     * This method will stop the App Tracking Protection feature
     */
    fun stop()

    /**
     * This is a suspend function because the operation can take time.
     * You DO NOT need to set any dispatcher to call this suspend function
     *
     * @return a list of app packages that is excluded from App Tracking Protection
     */
    suspend fun getExcludedApps(): List<String>
}
