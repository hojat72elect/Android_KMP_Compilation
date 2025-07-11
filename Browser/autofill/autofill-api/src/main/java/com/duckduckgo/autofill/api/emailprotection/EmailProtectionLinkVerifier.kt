

package com.duckduckgo.autofill.api.emailprotection

/**
 * Determines if a link should be consumed by the in-context Email Protection feature.
 *
 * If the link should be consumed, it will be delegated to the in-context view.
 * If the link should not be consumed, it should open as normal in the browser.
 *
 */
interface EmailProtectionLinkVerifier {

    /**
     * Determines if a link should be consumed by the in-context Email Protection feature or opened as a normal URL in the browser.
     *
     * @param url The url which will be checked to determine if a verification link or not
     * @param inContextViewAlreadyShowing Whether the in-context view is already showing. If it is not showing, then
     * the link should not be consumed in-context.
     */
    fun shouldDelegateToInContextView(
        url: String?,
        inContextViewAlreadyShowing: Boolean?,
    ): Boolean
}
