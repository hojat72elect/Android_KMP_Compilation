

package com.duckduckgo.autofill.impl

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(AppScope::class)
interface PasswordStoreEventListener {
    /**
     * Called when a credential is added to the password store
     * This includes one being automatically saved, the user manually saving one, or one being imported via sync.
     *
     * @param id the id of the credential that was added
     */
    fun onCredentialAdded(id: Long) {}
}
