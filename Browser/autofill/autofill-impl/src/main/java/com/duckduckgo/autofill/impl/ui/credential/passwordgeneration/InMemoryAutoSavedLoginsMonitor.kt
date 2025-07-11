

package com.duckduckgo.autofill.impl.ui.credential.passwordgeneration

import com.duckduckgo.autofill.api.passwordgeneration.AutomaticSavedLoginsMonitor
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

// singleton in app scope, as we want the same instance across multiple fragments
@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class InMemoryAutoSavedLoginsMonitor @Inject constructor() : AutomaticSavedLoginsMonitor {

    private val autoSavedLoginsPerTab = mutableMapOf<String, Long>()

    override fun getAutoSavedLoginId(tabId: String?): Long? = autoSavedLoginsPerTab[tabId]

    override fun setAutoSavedLoginId(
        value: Long,
        tabId: String?,
    ) {
        tabId?.let { autoSavedLoginsPerTab[it] = value }
    }

    override fun clearAutoSavedLoginId(tabId: String?) {
        tabId?.let {
            autoSavedLoginsPerTab.remove(it)
        }
    }
}
