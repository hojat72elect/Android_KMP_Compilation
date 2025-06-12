@file:Suppress("SpellCheckingInspection")

package com.amaze.fileutilities.utilis.dialog_picker

import android.content.Context
import com.afollestad.materialdialogs.files.FileFilter
import java.io.File

internal fun File.hasParent(
    context: Context,
    writeable: Boolean,
    filter: FileFilter
) = betterParent(context, writeable, filter) != null

internal fun File.isExternalStorage(context: Context) =
    absolutePath == context.getExternalFilesDir()?.absolutePath

internal fun File.isRoot() = absolutePath == "/"

internal fun File.betterParent(
    context: Context,
    writeable: Boolean,
    filter: FileFilter
): File? {
    val parentToUse = (
            if (isExternalStorage(context)) {
                // Emulated external storage's parent is empty so jump over it
                context.getExternalFilesDir()?.parentFile?.parentFile
            } else {
                parentFile
            }
            ) ?: return null

    if ((writeable && !parentToUse.canWrite()) || !parentToUse.canRead()) {
        // We can't access this folder
        return null
    }

    val folderContent =
        parentToUse.listFiles()?.filter { filter?.invoke(it) ?: true } ?: emptyList()
    if (folderContent.isEmpty()) {
        // There is nothing in this folder most likely because we can't access files inside of it.
        // We don't want to get stuck here.
        return null
    }

    return parentToUse
}

internal fun File.jumpOverEmulated(context: Context): File {
    val externalFileDir = context.getExternalFilesDir()
    externalFileDir?.parentFile?.let { externalParentFile ->
        if (absolutePath == externalParentFile.absolutePath) {
            return externalFileDir
        }
    }
    return this
}

internal fun File.friendlyName(context: Context) = when {
    isExternalStorage(context) -> "External Storage"
    isRoot() -> "Root"
    else -> name
}
