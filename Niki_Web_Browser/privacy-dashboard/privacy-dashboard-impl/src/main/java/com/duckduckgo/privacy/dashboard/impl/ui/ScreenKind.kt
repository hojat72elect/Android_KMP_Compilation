

package com.duckduckgo.privacy.dashboard.impl.ui

/**
 * Based on https://duckduckgo.github.io/privacy-dashboard/types/Generated_Schema_Definitions.ScreenKind.html
 */
enum class ScreenKind(val value: String) {
    PRIMARY_SCREEN("primaryScreen"),
    BREAKAGE_FORM("breakageForm"),
    PROMPT_BREAKAGE_FORM("promptBreakageForm"),
    TOGGLE_REPORT("toggleReport"),
    CATEGORY_TYPE_SELECTION("categoryTypeSelection"),
    CATEGORY_SELECTION("categorySelection"),
    CHOICE_TOGGLE("choiceToggle"),
    CHOICE_BREAKAGE_FORM("choiceBreakageForm"),
    CONNECTION("connection"),
    TRACKERS("trackers"),
    NON_TRACKERS("nonTrackers"),
    CONSENT_MANAGED("consentManaged"),
    COOKIE_HIDDEN("cookieHidden"),
    ;
}
