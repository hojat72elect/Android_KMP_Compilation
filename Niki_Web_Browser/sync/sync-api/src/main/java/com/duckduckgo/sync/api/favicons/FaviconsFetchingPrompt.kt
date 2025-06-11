

package com.duckduckgo.sync.api.favicons

interface FaviconsFetchingPrompt {
    fun shouldShow(): Boolean

    fun onPromptAnswered(fetchingEnabled: Boolean)
}
