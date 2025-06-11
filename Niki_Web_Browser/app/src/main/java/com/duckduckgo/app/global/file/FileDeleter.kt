

package com.duckduckgo.app.global.file

import com.duckduckgo.common.utils.DispatcherProvider
import java.io.File
import kotlinx.coroutines.withContext
import timber.log.Timber

interface FileDeleter {

    /**
     * Delete the contents of the given directory, but don't delete the directory itself
     *
     * Optionally: specify an exclusion list. Files with names exactly matching will not be deleted.
     * Note, the exclusion list only applies to the top-level directory. All files in subdirectories will be deleted, regardless of exclusion list.
     */
    suspend fun deleteContents(
        parentDirectory: File,
        excludedFiles: List<String> = emptyList(),
    )

    /**
     * Delete the contents of the given directory, and deletes the directory itself.
     */
    suspend fun deleteDirectory(directoryToDelete: File)

    /**
     * Delete a file(s) of the given directory, but don't delete the directory itself
     */
    suspend fun deleteFilesFromDirectory(
        parentDirectory: File,
        files: List<String>,
    )
}

class AndroidFileDeleter(private val dispatchers: DispatcherProvider) : FileDeleter {
    override suspend fun deleteContents(
        parentDirectory: File,
        excludedFiles: List<String>,
    ) {
        withContext(dispatchers.io()) {
            runCatching {
                val files = parentDirectory.listFiles() ?: return@withContext
                val filesToDelete = files.filterNot { excludedFiles.contains(it.name) }
                filesToDelete.forEach { it.deleteRecursively() }
            }.onFailure {
                Timber.e(it, "Failed to delete contents of directory: %s", parentDirectory.absolutePath)
            }
        }
    }

    override suspend fun deleteDirectory(directoryToDelete: File) {
        withContext(dispatchers.io()) {
            directoryToDelete.deleteRecursively()
        }
    }

    override suspend fun deleteFilesFromDirectory(
        parentDirectory: File,
        files: List<String>,
    ) {
        withContext(dispatchers.io()) {
            val allFiles = parentDirectory.listFiles() ?: return@withContext
            val filesToDelete = allFiles.filter { files.contains(it.name) }
            filesToDelete.forEach { it.deleteRecursively() }
        }
    }
}
