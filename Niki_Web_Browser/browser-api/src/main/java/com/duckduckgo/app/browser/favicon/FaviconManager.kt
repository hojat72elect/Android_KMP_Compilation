

package com.duckduckgo.app.browser.favicon

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import java.io.File

interface FaviconManager {
    suspend fun storeFavicon(
        tabId: String,
        faviconSource: FaviconSource,
    ): File?

    suspend fun tryFetchFaviconForUrl(
        tabId: String,
        url: String,
    ): File?

    suspend fun tryFetchFaviconForUrl(
        url: String,
    ): File?

    suspend fun persistCachedFavicon(
        tabId: String,
        url: String,
    )

    suspend fun loadToViewMaybeFromRemoteWithPlaceholder(
        url: String,
        view: ImageView,
        placeholder: String? = null,
    )

    suspend fun loadToViewFromLocalWithPlaceholder(
        tabId: String? = null,
        url: String,
        view: ImageView,
        placeholder: String? = null,
    )

    suspend fun loadFromDisk(
        tabId: String?,
        url: String,
    ): Bitmap?

    suspend fun loadFromDiskWithParams(
        tabId: String? = null,
        url: String,
        cornerRadius: Int,
        width: Int,
        height: Int,
    ): Bitmap?

    suspend fun deletePersistedFavicon(url: String)
    suspend fun deleteOldTempFavicon(
        tabId: String,
        path: String?,
    )

    suspend fun deleteAllTemp()

    /**
     * Generates a drawable which can be used as a placeholder for a favicon when a real one cannot be found
     * @param placeholder the placeholder text to be used. if null, the placeholder letter will be extracted from the domain
     * @param domain the domain of the site for which the favicon is being generated, used to generate background color
     */
    fun generateDefaultFavicon(
        placeholder: String?,
        domain: String,
    ): Drawable
}

sealed class FaviconSource {
    data class ImageFavicon(
        val icon: Bitmap,
        val url: String,
    ) : FaviconSource()

    data class UrlFavicon(
        val faviconUrl: String,
        val url: String,
    ) : FaviconSource()
}
