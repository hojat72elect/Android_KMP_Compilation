

package com.duckduckgo.savedsites.api.service

import android.net.Uri

interface SavedSitesManager {
    suspend fun import(uri: Uri): ImportSavedSitesResult
    suspend fun export(uri: Uri): ExportSavedSitesResult
}
