

package com.duckduckgo.app.statistics.api

interface AtbLifecyclePlugin {
    /**
     * Will be called right after we have refreshed the ATB retention on search
     */
    fun onSearchRetentionAtbRefreshed(oldAtb: String, newAtb: String) {
        // default is no-op
    }

    /**
     * Will be called right after we have refreshed the ATB retention on search
     */
    fun onAppRetentionAtbRefreshed(oldAtb: String, newAtb: String) {
        // default is no-op
    }

    /**
     * Will be called right after the ATB is first initialized and successfully sent via exti call
     */
    fun onAppAtbInitialized() {
        // default is no-op
    }
}
