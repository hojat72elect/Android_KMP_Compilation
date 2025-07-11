

package com.duckduckgo.browser.api

import com.duckduckgo.common.ui.DuckDuckGoTheme
import java.util.*

interface UserBrowserProperties {
    fun appTheme(): DuckDuckGoTheme
    suspend fun bookmarks(): Long
    suspend fun favorites(): Long
    fun daysSinceInstalled(): Long
    suspend fun daysUsedSince(since: Date): Long
    fun defaultBrowser(): Boolean
    fun emailEnabled(): Boolean
    fun searchCount(): Long
    fun widgetAdded(): Boolean
}
