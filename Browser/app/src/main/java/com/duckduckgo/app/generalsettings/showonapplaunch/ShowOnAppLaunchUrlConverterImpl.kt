

package com.duckduckgo.app.generalsettings.showonapplaunch

import android.net.Uri
import com.duckduckgo.app.generalsettings.showonapplaunch.store.ShowOnAppLaunchOptionDataStore

class ShowOnAppLaunchUrlConverterImpl : UrlConverter {

    override fun convertUrl(url: String?): String {
        if (url.isNullOrBlank()) return ShowOnAppLaunchOptionDataStore.DEFAULT_SPECIFIC_PAGE_URL

        val uri = Uri.parse(url.trim())

        val uriWithScheme = if (uri.scheme == null) {
            Uri.Builder()
                .scheme("http")
                .authority(uri.path?.lowercase())
        } else {
            uri.buildUpon()
                .scheme(uri.scheme?.lowercase())
                .authority(uri.authority?.lowercase())
        }
            .apply {
                query(uri.query)
                fragment(uri.fragment)
            }

        val uriWithPath = if (uri.path.isNullOrBlank()) {
            uriWithScheme.path("/")
        } else {
            uriWithScheme
        }

        val processedUrl = uriWithPath.build().toString()

        return Uri.decode(processedUrl)
    }
}
