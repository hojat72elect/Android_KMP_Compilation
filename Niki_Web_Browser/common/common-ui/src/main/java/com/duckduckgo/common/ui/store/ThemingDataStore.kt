

package com.duckduckgo.common.ui.store

import com.duckduckgo.common.ui.DuckDuckGoTheme

interface ThemingDataStore {

    var theme: DuckDuckGoTheme

    fun isCurrentlySelected(theme: DuckDuckGoTheme): Boolean
}
