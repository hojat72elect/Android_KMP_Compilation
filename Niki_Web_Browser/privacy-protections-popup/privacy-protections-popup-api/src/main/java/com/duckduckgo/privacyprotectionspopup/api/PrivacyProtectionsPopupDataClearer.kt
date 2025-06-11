

package com.duckduckgo.privacyprotectionspopup.api

/**
 * Interface for clearing any personal data that may be managed by Privacy Protections Popup.
 * Should be used whenever the "fire" action is invoked.
 */
interface PrivacyProtectionsPopupDataClearer {

    /**
     * Deletes any personal data managed by this module (e.g., domains, for which the popup was shown).
     */
    suspend fun clearPersonalData()
}
