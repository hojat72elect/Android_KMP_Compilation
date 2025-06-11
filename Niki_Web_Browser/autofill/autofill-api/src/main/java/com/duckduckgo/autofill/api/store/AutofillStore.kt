

package com.duckduckgo.autofill.api.store

import com.duckduckgo.autofill.api.domain.app.LoginCredentials

/**
 * Public APIs for querying credentials stored in the autofill store
 */
interface AutofillStore {

    /**
     * Find saved credentials for the given URL, returning an empty list where no matches are found
     * @param rawUrl Can be a full, unmodified URL taken from the URL bar (containing subdomains, query params etc...)
     */
    suspend fun getCredentials(rawUrl: String): List<LoginCredentials>
}
