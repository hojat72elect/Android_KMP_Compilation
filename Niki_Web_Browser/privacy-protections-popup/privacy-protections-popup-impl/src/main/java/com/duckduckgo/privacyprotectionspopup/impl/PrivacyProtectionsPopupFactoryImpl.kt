

package com.duckduckgo.privacyprotectionspopup.impl

import android.view.View
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.privacyprotectionspopup.api.PrivacyProtectionsPopup
import com.duckduckgo.privacyprotectionspopup.api.PrivacyProtectionsPopupFactory
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(FragmentScope::class)
class PrivacyProtectionsPopupFactoryImpl @Inject constructor() : PrivacyProtectionsPopupFactory {

    override fun createPopup(anchor: View): PrivacyProtectionsPopup {
        return PrivacyProtectionsPopupImpl(anchor)
    }
}
