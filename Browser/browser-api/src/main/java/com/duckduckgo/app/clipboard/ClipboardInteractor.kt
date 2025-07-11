

package com.duckduckgo.app.clipboard

/**
 * Used for copying to the clipboard.
 */
interface ClipboardInteractor {
    /**
     * Copies the given text to the clipboard.
     * @param toCopy The text to copy.
     * @param isSensitive Whether the text is sensitive or not.
     * @return Returns true if a notification was shown automatically to the user. This happens on some Android versions, and we don't want to double-notify.
     */
    fun copyToClipboard(toCopy: String, isSensitive: Boolean): Boolean
}
