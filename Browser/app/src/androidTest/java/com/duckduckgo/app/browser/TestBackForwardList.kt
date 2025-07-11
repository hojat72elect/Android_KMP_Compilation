

package com.duckduckgo.app.browser

import android.webkit.WebBackForwardList
import android.webkit.WebHistoryItem

class TestBackForwardList : WebBackForwardList() {
    private val fakeHistory: MutableList<WebHistoryItem> = mutableListOf()
    private var fakeCurrentIndex = -1

    fun addPageToHistory(webHistoryItem: WebHistoryItem) {
        fakeHistory.add(webHistoryItem)
        fakeCurrentIndex++
    }

    override fun getSize() = fakeHistory.size

    override fun getItemAtIndex(index: Int): WebHistoryItem = fakeHistory[index]

    override fun getCurrentItem(): WebHistoryItem? = null

    override fun getCurrentIndex(): Int = fakeCurrentIndex

    override fun clone(): WebBackForwardList = throw NotImplementedError()
}
