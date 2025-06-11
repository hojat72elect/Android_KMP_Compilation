

package com.duckduckgo.duckchat.api

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

/**
 * DuckChat interface provides a set of methods for interacting and controlling DuckChat.
 */
interface DuckChat {
    /**
     * Checks whether DuckChat is enabled based on remote config flag.
     * Uses a cached value - does not perform disk I/O.
     *
     * @return true if DuckChat is enabled, false otherwise.
     */
    fun isEnabled(): Boolean

    /**
     * Checks whether DuckChat should be shown in browser menu based on user settings.
     *
     * @return true if DuckChat should be shown, false otherwise.
     */
    val showInBrowserMenu: StateFlow<Boolean>

    /**
     * Checks whether DuckChat should be shown in address bar based on user settings.
     *
     * @return true if DuckChat should be shown, false otherwise.
     */
    val showInAddressBar: StateFlow<Boolean>

    /**
     * Opens the DuckChat WebView with optional pre-filled [String] query.
     */
    fun openDuckChat(query: String? = null)

    /**
     * Auto-prompts the DuckChat WebView with the provided [String] query.
     */
    fun openDuckChatWithAutoPrompt(query: String)

    /**
     * Determines whether a given [Uri] is a DuckChat URL.
     *
     * @return true if it is a DuckChat URL, false otherwise.
     */
    fun isDuckChatUrl(uri: Uri): Boolean

    /**
     * Returns `true` if Duck Chat was ever opened before.
     */
    suspend fun wasOpenedBefore(): Boolean
}
