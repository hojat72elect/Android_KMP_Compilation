

package com.duckduckgo.app.tabs.ui

import android.content.Context
import android.net.Uri
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.tabs.model.TabEntity
import com.duckduckgo.app.tabs.model.isBlank
import com.duckduckgo.common.utils.AppUrl

fun TabEntity.displayTitle(context: Context): String {
    if (isBlank) {
        return context.getString(R.string.newTabMenuItem)
    }

    return title ?: Uri.parse(resolvedUrl()).host ?: ""
}

private fun TabEntity.resolvedUrl(): String {
    return if (isBlank) AppUrl.Url.HOME else url ?: ""
}

fun TabEntity.displayUrl(): String {
    return resolvedUrl()
}
