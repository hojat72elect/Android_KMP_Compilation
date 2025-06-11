

package com.duckduckgo.savedsites.impl.service

import android.net.Uri
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.app.statistics.pixels.Pixel.PixelParameter.BOOKMARK_COUNT
import com.duckduckgo.savedsites.api.service.ExportSavedSitesResult
import com.duckduckgo.savedsites.api.service.ImportSavedSitesResult
import com.duckduckgo.savedsites.api.service.SavedSitesExporter
import com.duckduckgo.savedsites.api.service.SavedSitesImporter
import com.duckduckgo.savedsites.api.service.SavedSitesManager
import com.duckduckgo.savedsites.impl.SavedSitesPixelName

class RealSavedSitesManager constructor(
    private val savedSitesImporter: SavedSitesImporter,
    private val savedSitesExporter: SavedSitesExporter,
    private val pixel: Pixel,
) : SavedSitesManager {

    override suspend fun export(uri: Uri): ExportSavedSitesResult {
        val result = savedSitesExporter.export(uri)
        when (result) {
            is ExportSavedSitesResult.Error -> {
                pixel.fire(SavedSitesPixelName.BOOKMARK_EXPORT_ERROR)
            }
            ExportSavedSitesResult.NoSavedSitesExported -> {}
            ExportSavedSitesResult.Success -> {
                pixel.fire(SavedSitesPixelName.BOOKMARK_EXPORT_SUCCESS)
            }
        }
        return result
    }

    override suspend fun import(uri: Uri): ImportSavedSitesResult {
        val result = savedSitesImporter.import(uri)
        when (result) {
            is ImportSavedSitesResult.Error -> {
                pixel.fire(SavedSitesPixelName.BOOKMARK_IMPORT_ERROR)
            }
            is ImportSavedSitesResult.Success -> {
                pixel.fire(SavedSitesPixelName.BOOKMARK_IMPORT_SUCCESS, mapOf(BOOKMARK_COUNT to result.savedSites.size.toString()))
            }
        }
        return result
    }
}
