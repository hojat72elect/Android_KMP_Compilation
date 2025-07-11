

package com.duckduckgo.daxprompts.api

/**
 * DaxPrompts interface provides a set of methods for controlling the display for various Dax Prompts in the app.
 */
interface DaxPrompts {

    suspend fun evaluate(): ActionType

    enum class ActionType {
        SHOW_CONTROL,
        SHOW_VARIANT_DUCKPLAYER,
        SHOW_VARIANT_BROWSER_COMPARISON,
        NONE,
    }
}
