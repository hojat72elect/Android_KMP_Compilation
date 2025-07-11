

package com.duckduckgo.savedsites.api.service

import android.net.Uri

/**
 * Class that takes care of importing [SavedSites]
 * This is used to import [SavedSites] from another Browser
 */
interface SavedSitesImporter {
    /**
     * Reads a HTML based file with all [SavedSites] that the user has
     * in Netscape format.
     * @param uri of the [File] we'll read the data from
     * @return [ImportSavedSitesResult] result of the operation
     */
    suspend fun import(uri: Uri): ImportSavedSitesResult
}

sealed class ImportSavedSitesResult {
    data class Success(val savedSites: List<Any>) : ImportSavedSitesResult()
    data class Error(val exception: Exception) : ImportSavedSitesResult()
}
