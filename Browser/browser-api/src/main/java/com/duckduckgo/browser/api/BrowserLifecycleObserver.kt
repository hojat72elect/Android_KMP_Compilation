

package com.duckduckgo.browser.api

/**
 * Implement this interface and contribute it as a multibinding if you want to get callbacks
 * about the lifecycle of the DDG Browser application.
 */
interface BrowserLifecycleObserver {
    /**
     * Called once when the application is opened.
     * [isFreshLaunch] will be `true` if it is a fresh launch, `false` otherwise
     */
    fun onOpen(isFreshLaunch: Boolean) {}

    /**
     * Called every time the application is foregrounded
     */
    fun onForeground() {}

    /**
     * Called every time the application is backgrounded
     */
    fun onBackground() {}

    /**
     * Called when the application is closed.
     * Close means that the application does not have any activity in STARTED state, however it may have
     * activities in CREATED state. Examples are:
     * * when user homes the app
     * * when user swipes-closes the app
     *
     * see also [BrowserLifecycleObserver.onExit]
     */
    fun onClose() {}

    /**
     * Called when the application exits.
     * Exit means that the application does NOT have any activity in CREATED state.
     * This call will always follow the [BrowserLifecycleObserver.onClose] call
     */
    fun onExit() {}
}
