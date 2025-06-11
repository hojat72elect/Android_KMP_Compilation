

package com.duckduckgo.autofill.api

/**
 * Public API that could be used if the user is a verified internal tester.
 * This class could potentially be moved to a different module once there's a need for it in the future.
 */
interface InternalTestUserChecker {
    /**
     * This checks if the user has went through the process of becoming a verified test user
     */
    val isInternalTestUser: Boolean

    /**
     * This method should be called if an error is received when loading a [url].
     * This will only be processed if the [url] passed is a valid internal tester success verification url
     * else it will just be ignored.
     */
    fun verifyVerificationErrorReceived(url: String)

    /**
     * This method should be called if the [url] is completely loaded.
     * This will only be processed if the [url] passed is a valid internal tester success verification url
     * else it will just be ignored.
     */
    fun verifyVerificationCompleted(url: String)
}
