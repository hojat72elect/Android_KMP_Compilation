

package com.duckduckgo.autofill.api.domain.app

import android.os.Parcelable
import java.io.Serializable
import kotlinx.parcelize.Parcelize

/**
 * Representation of login credentials used for autofilling into the browser.
 */
@Parcelize
data class LoginCredentials(
    val id: Long? = null,
    val domain: String?,
    val username: String?,
    val password: String?,
    val domainTitle: String? = null,
    val notes: String? = null,
    val lastUpdatedMillis: Long? = null,
    val lastUsedMillis: Long? = null,
) : Parcelable, Serializable {
    override fun toString(): String {
        return """
            LoginCredentials(
                id=$id, domain=$domain, username=$username, password=********, domainTitle=$domainTitle, 
                lastUpdatedMillis=$lastUpdatedMillis, lastUsedMillis=$lastUsedMillis, notesLength=${notes?.length ?: 0}
            )
        """.trimIndent()
    }
}
