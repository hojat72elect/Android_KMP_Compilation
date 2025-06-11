

package com.duckduckgo.autofill.impl.importing

/**
 * Data class representing the login credentials imported from a Google CSV file.
 */
data class GoogleCsvLoginCredential(
    val url: String? = null,
    val username: String? = null,
    val password: String? = null,
    val title: String? = null,
    val notes: String? = null,
)
