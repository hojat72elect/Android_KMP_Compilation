

package com.duckduckgo.autofill.api.credential.saving

/**
 * Allows for the creation of a login when a private duck address alias is used
 */
interface DuckAddressLoginCreator {

    /**
     * Creates a login for the given duck address, where username matches duck address and password is empty
     * @param duckAddress the duck address to create a login for
     * @param tabId the tab id of the tab that the duck address was used in
     * @param originalUrl the original url that the duck address was used in
     */
    fun createLoginForPrivateDuckAddress(
        duckAddress: String,
        tabId: String,
        originalUrl: String,
    )
}
