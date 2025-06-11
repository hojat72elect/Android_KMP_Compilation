

package com.duckduckgo.privacyprotectionspopup.impl

import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.privacyprotectionspopup.impl.db.PopupDismissDomainRepository
import com.duckduckgo.privacyprotectionspopup.impl.store.PrivacyProtectionsPopupDataStore
import com.squareup.anvil.annotations.ContributesBinding
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface PrivacyProtectionsPopupManagerDataProvider {
    fun getData(domain: String): Flow<PrivacyProtectionsPopupManagerData>
}

data class PrivacyProtectionsPopupManagerData(
    val protectionsEnabled: Boolean,
    val popupDismissedAt: Instant?,
    val toggleUsedAt: Instant?,
    val popupTriggerCount: Int,
    val doNotShowAgainClicked: Boolean,
    val experimentVariant: PrivacyProtectionsPopupExperimentVariant?,
)

@ContributesBinding(FragmentScope::class)
class PrivacyProtectionsPopupManagerDataProviderImpl @Inject constructor(
    private val protectionsStateProvider: ProtectionsStateProvider,
    private val popupDismissDomainRepository: PopupDismissDomainRepository,
    private val dataStore: PrivacyProtectionsPopupDataStore,
) : PrivacyProtectionsPopupManagerDataProvider {

    override fun getData(domain: String): Flow<PrivacyProtectionsPopupManagerData> =
        combine(
            protectionsStateProvider.areProtectionsEnabled(domain),
            popupDismissDomainRepository.getPopupDismissTime(domain),
            dataStore.data,
        ) { protectionsEnabled, popupDismissedAt, popupData ->
            PrivacyProtectionsPopupManagerData(
                protectionsEnabled = protectionsEnabled,
                popupDismissedAt = popupDismissedAt,
                toggleUsedAt = popupData.toggleUsedAt,
                popupTriggerCount = popupData.popupTriggerCount,
                doNotShowAgainClicked = popupData.doNotShowAgainClicked,
                experimentVariant = popupData.experimentVariant,
            )
        }
}
