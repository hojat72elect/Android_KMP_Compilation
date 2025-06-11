

package com.duckduckgo.privacyprotectionspopup.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacyprotectionspopup.api.PrivacyProtectionsToggleUsageListener
import com.duckduckgo.privacyprotectionspopup.impl.store.PrivacyProtectionsPopupDataStore
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class PrivacyProtectionsToggleUsageListenerImpl @Inject constructor(
    private val timeProvider: TimeProvider,
    private val dataStore: PrivacyProtectionsPopupDataStore,
) : PrivacyProtectionsToggleUsageListener {

    override suspend fun onPrivacyProtectionsToggleUsed() {
        dataStore.setToggleUsageTimestamp(timeProvider.getCurrentTime())
    }
}
