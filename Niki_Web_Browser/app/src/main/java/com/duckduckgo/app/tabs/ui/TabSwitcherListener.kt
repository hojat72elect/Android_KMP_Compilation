

package com.duckduckgo.app.tabs.ui

interface TabSwitcherListener {
    fun onNewTabRequested(fromOverflowMenu: Boolean)
    fun onTabSelected(tabId: String)
    fun onTabDeleted(position: Int, deletedBySwipe: Boolean)
    fun onTabMoved(from: Int, to: Int)
}
