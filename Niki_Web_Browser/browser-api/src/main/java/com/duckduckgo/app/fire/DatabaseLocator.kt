

package com.duckduckgo.app.fire

import android.content.Context
import java.io.File

abstract class DatabaseLocator(private val context: Context) {

    abstract val knownLocations: List<String>

    open fun getDatabasePath(): String {
        val dataDir = context.applicationInfo.dataDir
        val detectedPath = knownLocations.find { knownPath ->
            val file = File(dataDir, knownPath)
            file.exists()
        }

        return detectedPath
            .takeUnless { it.isNullOrEmpty() }
            ?.let { nonEmptyPath ->
                "$dataDir$nonEmptyPath"
            }.orEmpty()
    }
}
