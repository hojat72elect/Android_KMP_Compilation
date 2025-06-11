

package com.duckduckgo.autofill.api

/**
 * Used to check the status of various Autofill features.
 *
 * Whether autofill features are enabled depends on a variety of inputs. This class provides a single way to query the status of all of them.
 */
interface AutofillCapabilityChecker {

    /**
     * Whether autofill can inject credentials into a WebView for the given page.
     * @param url The URL of the webpage to check.
     */
    suspend fun canInjectCredentialsToWebView(url: String): Boolean

    /**
     * Whether autofill can save credentials from a WebView for the given page.
     * @param url The URL of the webpage to check.
     */
    suspend fun canSaveCredentialsFromWebView(url: String): Boolean

    /**
     * Whether autofill can generate a password into a WebView for the given page.
     * @param url The URL of the webpage to check.
     */
    suspend fun canGeneratePasswordFromWebView(url: String): Boolean

    /**
     * Whether a user can access the credential management screen.
     */
    suspend fun canAccessCredentialManagementScreen(): Boolean

    /**
     * Whether autofill is configured to be enabled. This is a configuration value, not a user preference.
     */
    suspend fun isAutofillEnabledByConfiguration(url: String): Boolean
}
