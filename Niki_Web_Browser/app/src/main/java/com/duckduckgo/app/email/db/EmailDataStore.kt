

package com.duckduckgo.app.email.db

/**
 * Data store for Email Protection (duck addresses)
 *
 * Provides ability to store and retrieve data related to the duck address feature such as personal username, next alias etc...
 */
interface EmailDataStore {
    var emailToken: String?
    var nextAlias: String?
    var emailUsername: String?
    var cohort: String?
    var lastUsedDate: String?
    fun canUseEncryption(): Boolean
}
