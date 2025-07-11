

package com.duckduckgo.privacyprotectionspopup.impl.db

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacyprotectionspopup.api.PrivacyProtectionsPopupDataClearer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class PrivacyProtectionsPopupDataClearerImpl @Inject constructor(
    private val popupDismissDomainsRepository: PopupDismissDomainRepository,
) : PrivacyProtectionsPopupDataClearer {

    override suspend fun clearPersonalData() {
        popupDismissDomainsRepository.removeAllEntries()
    }
}
