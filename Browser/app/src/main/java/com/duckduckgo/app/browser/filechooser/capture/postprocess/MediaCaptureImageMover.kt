

package com.duckduckgo.app.browser.filechooser.capture.postprocess

import android.content.Context
import android.net.Uri
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.FragmentScope
import com.squareup.anvil.annotations.ContributesBinding
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface MediaCaptureImageMover {
    suspend fun moveInternal(contentUri: Uri): File?
}

@ContributesBinding(FragmentScope::class)
class RealMediaCaptureImageMover @Inject constructor(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : MediaCaptureImageMover {

    override suspend fun moveInternal(contentUri: Uri): File? {
        return withContext(dispatchers.io()) {
            val newDestinationDirectory = File(context.cacheDir, SUBDIRECTORY_NAME)
            newDestinationDirectory.mkdirs()

            val filename = contentUri.lastPathSegment ?: return@withContext null

            val newDestinationFile = File(newDestinationDirectory, filename)
            context.contentResolver.openInputStream(contentUri)?.use { inputStream ->
                FileOutputStream(newDestinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            newDestinationFile
        }
    }

    companion object {
        private const val SUBDIRECTORY_NAME = "browser-uploads"
    }
}
