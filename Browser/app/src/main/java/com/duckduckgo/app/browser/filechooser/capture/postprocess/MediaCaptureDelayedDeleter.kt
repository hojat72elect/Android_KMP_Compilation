

package com.duckduckgo.app.browser.filechooser.capture.postprocess

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.duckduckgo.di.scopes.FragmentScope
import com.squareup.anvil.annotations.ContributesBinding
import java.io.File
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

interface MediaCaptureDelayedDeleter {
    fun scheduleDeletion(file: File)
}

@ContributesBinding(FragmentScope::class)
class WorkManagerMediaCaptureDelayedDeleter @Inject constructor(
    private val workManager: WorkManager,
) : MediaCaptureDelayedDeleter {

    override fun scheduleDeletion(file: File) {
        val workRequest = OneTimeWorkRequestBuilder<DeleteMediaCaptureWorker>()
            .setInputData(
                workDataOf(
                    DeleteMediaCaptureWorker.KEY_FILE_URI to file.absolutePath,
                ),
            )
            .setInitialDelay(INITIAL_DELAY_SECONDS, SECONDS)
            .build()
        workManager.enqueue(workRequest)
    }

    companion object {
        private const val INITIAL_DELAY_SECONDS = 60L
    }
}
