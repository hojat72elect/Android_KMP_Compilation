

package com.duckduckgo.espresso

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback

class WaitTimeIdlingResource(private val waitTimeInMillis: Long) : IdlingResource {
    private val startTime: Long = System.currentTimeMillis()
    private lateinit var resourceCallback: ResourceCallback

    override fun getName(): String = javaClass.name

    override fun isIdleNow(): Boolean {
        val elapsed = System.currentTimeMillis() - startTime
        val idle: Boolean = elapsed >= waitTimeInMillis
        if (idle) {
            resourceCallback.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        this.resourceCallback = callback
    }
}
