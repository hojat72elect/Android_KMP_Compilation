

package com.duckduckgo.autofill.api.email

import kotlinx.coroutines.flow.StateFlow

/**
 * Provides ability to store and retrieve data related to the duck address feature such as personal username, if signed in, next alias etc...
 */
interface EmailManager {

    /**
     * Indicates if the user is signed in or not. This is a flow so that it can be observed.
     */
    fun signedInFlow(): StateFlow<Boolean>

    /**
     * Indicates if the user is signed in or not
     */
    fun isSignedIn(): Boolean

    /**
     * Get the next available private duck address alias
     */
    fun getAlias(): String?

    /**
     * Get the stored auth token
     */
    fun getToken(): String?

    /**
     * Store the credentials for the user's duck address
     */
    fun storeCredentials(
        token: String,
        username: String,
        cohort: String,
    )

    /**
     * Signs out of using duck addresses on this device
     */
    fun signOut()

    /**
     * Get the user's full, personal duck address
     */
    fun getEmailAddress(): String?

    /**
     * Get the user's duck address data in a format that can be passed to JS
     */
    fun getUserData(): String

    /**
     * Get the cohort
     */
    fun getCohort(): String

    /**
     * Determines if duck address can be used on this device
     */
    fun isEmailFeatureSupported(): Boolean

    /**
     * Return last used date
     */
    fun getLastUsedDate(): String

    /**
     * Updates the last used date
     */
    fun setNewLastUsedDate()
}
