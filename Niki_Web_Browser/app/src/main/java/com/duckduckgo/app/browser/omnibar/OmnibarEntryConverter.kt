

package com.duckduckgo.app.browser.omnibar

interface OmnibarEntryConverter {

    fun convertQueryToUrl(
        searchQuery: String,
        vertical: String? = null,
        queryOrigin: QueryOrigin = QueryOrigin.FromUser,
    ): String
}

sealed class QueryOrigin {
    object FromUser : QueryOrigin()
    data class FromAutocomplete(val isNav: Boolean?) : QueryOrigin()
}
