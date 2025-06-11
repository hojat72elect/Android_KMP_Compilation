

package com.duckduckgo.downloads.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.io.File
import javax.inject.Inject

interface MediaScanner {
    /**
     * Triggers the Media Store scanner so that the newly added [File] is immediately visible to the user.
     */
    fun scan(file: File)
}

@ContributesBinding(AppScope::class)
class MediaScannerImpl @Inject constructor(
    private val context: Context,
) : MediaScanner {
    override fun scan(file: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = Uri.fromFile(file)
        context.sendBroadcast(mediaScanIntent)
    }
}
