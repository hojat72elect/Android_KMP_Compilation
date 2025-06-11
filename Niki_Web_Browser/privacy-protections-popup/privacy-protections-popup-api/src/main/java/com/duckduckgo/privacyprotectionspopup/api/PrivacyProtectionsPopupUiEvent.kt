

package com.duckduckgo.privacyprotectionspopup.api

/**
 * Enum class representing different UI events emitted by [PrivacyProtectionsPopup]
 * and consumed by [PrivacyProtectionsPopupManager].
 */
enum class PrivacyProtectionsPopupUiEvent {
    /**
     * Event indicating that the popup was dismissed by the user without explicit
     * interaction with the popup UI, e.g., by clicking outside of the popup.
     */
    DISMISSED,

    /**
     * Event indicating that the dismiss button within the popup was clicked.
     */
    DISMISS_CLICKED,

    /**
     * Event indicating that the 'Disable Protections' button was clicked.
     */
    DISABLE_PROTECTIONS_CLICKED,

    /**
     * Event indicating that the 'Don't show again' button was clicked.
     */
    DONT_SHOW_AGAIN_CLICKED,

    /**
     * Event indicating that the privacy dashboard icon (popup anchor view) was clicked.
     *
     * The click event is passed to the popup anchor view, so no extra handling is necessary
     * to open the privacy dashboard. This is emitted to ensure the [PrivacyProtectionsPopupManager]
     * state is updated and for measurement purposes.
     */
    PRIVACY_DASHBOARD_CLICKED,
}
