

package com.duckduckgo.privacyprotectionspopup.impl

import com.duckduckgo.di.scopes.FragmentScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface PrivacyProtectionsPopupPixels {
    fun reportExperimentVariantAssigned()
    fun reportPopupTriggered()
    fun reportProtectionsDisabled()
    fun reportPrivacyDashboardOpened()
    fun reportPopupDismissedViaButton()
    fun reportPopupDismissedViaClickOutside()
    fun reportDoNotShowAgainClicked()
    fun reportPageRefreshOnPossibleBreakage()
}

@ContributesBinding(FragmentScope::class)
class PrivacyProtectionsPopupPixelsImpl @Inject constructor() : PrivacyProtectionsPopupPixels {

    override fun reportExperimentVariantAssigned() {
    }

    override fun reportPopupTriggered() {
    }

    override fun reportProtectionsDisabled() {
    }

    override fun reportPrivacyDashboardOpened() {
    }

    override fun reportPopupDismissedViaButton() {
    }

    override fun reportPopupDismissedViaClickOutside() {
    }

    override fun reportDoNotShowAgainClicked() {
    }

    override fun reportPageRefreshOnPossibleBreakage() {
    }
}
