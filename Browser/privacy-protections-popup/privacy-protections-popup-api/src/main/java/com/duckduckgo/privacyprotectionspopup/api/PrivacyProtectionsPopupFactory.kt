

package com.duckduckgo.privacyprotectionspopup.api

import android.view.View

interface PrivacyProtectionsPopupFactory {

    /**
     * Creates instance of [PrivacyProtectionsPopup] that manages UI of the popup.
     *
     * @param anchor The view to which the popup should be anchored.
     */
    fun createPopup(anchor: View): PrivacyProtectionsPopup
}
