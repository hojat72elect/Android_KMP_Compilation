

package com.duckduckgo.privacyprotectionspopup.api

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing the UI layer of the Privacy Protections Popup.
 */
interface PrivacyProtectionsPopup {

    /**
     * A flow of UI events for the Privacy Protections Popup.
     *
     * This property emits [PrivacyProtectionsPopupUiEvent] objects representing
     * various UI interactions or state changes that occur within the popup.
     * Those events should be consumed by [PrivacyProtectionsPopupManager]
     */
    val events: Flow<PrivacyProtectionsPopupUiEvent>

    /**
     * Updates the view state of the popup.
     *
     * @param viewState The new view state to be set for the popup.
     */
    fun setViewState(viewState: PrivacyProtectionsPopupViewState)

    /**
     * Notifies the popup UI about configuration change and re-creates UI if necessary.
     */
    fun onConfigurationChanged()
}
