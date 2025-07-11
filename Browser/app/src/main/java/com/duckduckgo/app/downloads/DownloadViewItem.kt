

package com.duckduckgo.app.downloads

import com.duckduckgo.downloads.api.model.DownloadItem

sealed class DownloadViewItem {
    object Empty : DownloadViewItem()
    object NotifyMe : DownloadViewItem()
    data class Header(val text: String) : DownloadViewItem()
    data class Item(val downloadItem: DownloadItem) : DownloadViewItem()
}
