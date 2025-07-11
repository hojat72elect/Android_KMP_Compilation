

package com.duckduckgo.app.downloads

import com.duckduckgo.downloads.api.model.DownloadItem

interface DownloadsItemListener {

    fun onItemClicked(item: DownloadItem)

    fun onShareItemClicked(item: DownloadItem)

    fun onDeleteItemClicked(item: DownloadItem)

    fun onCancelItemClicked(item: DownloadItem)

    fun onItemVisibilityChanged(visible: Boolean)
}
