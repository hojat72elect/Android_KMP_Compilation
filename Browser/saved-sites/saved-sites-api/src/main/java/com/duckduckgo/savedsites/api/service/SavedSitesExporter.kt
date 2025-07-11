

package com.duckduckgo.savedsites.api.service

import android.net.Uri

/**
 * Class that takes care of exporting [SavedSites]
 * This is used to export [SavedSites] to another Browser
 */
interface SavedSitesExporter {
    /**
     * Generates a HTML based file with all [SavedSites] that the user has
     * in Netscape format.
     * @param uri of the [File] where we'll store the data
     * @return [ExportSavedSitesResult] result of the operation
     */
    suspend fun export(uri: Uri): ExportSavedSitesResult
}

sealed class ExportSavedSitesResult {
    object Success : ExportSavedSitesResult()
    data class Error(val exception: Exception) : ExportSavedSitesResult()
    object NoSavedSitesExported : ExportSavedSitesResult()
}
