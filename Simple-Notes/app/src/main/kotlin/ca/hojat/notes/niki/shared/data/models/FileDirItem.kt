package ca.hojat.notes.niki.shared.data.models

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Immutable
import com.bumptech.glide.signature.ObjectKey
import ca.hojat.notes.niki.shared.extensions.formatDate
import ca.hojat.notes.niki.shared.extensions.formatSize
import ca.hojat.notes.niki.shared.extensions.getAndroidSAFFileSize
import ca.hojat.notes.niki.shared.extensions.getDocumentFile
import ca.hojat.notes.niki.shared.extensions.getItemSize
import ca.hojat.notes.niki.shared.extensions.getParentPath
import ca.hojat.notes.niki.shared.extensions.getProperSize
import ca.hojat.notes.niki.shared.extensions.getSizeFromContentUri
import ca.hojat.notes.niki.shared.extensions.isImageFast
import ca.hojat.notes.niki.shared.extensions.isPathOnOTG
import ca.hojat.notes.niki.shared.extensions.isRestrictedSAFOnlyRoot
import ca.hojat.notes.niki.shared.extensions.isVideoFast
import ca.hojat.notes.niki.shared.extensions.normalizeString
import ca.hojat.notes.niki.shared.helpers.AlphanumericComparator
import ca.hojat.notes.niki.shared.helpers.SORT_BY_DATE_MODIFIED
import ca.hojat.notes.niki.shared.helpers.SORT_BY_EXTENSION
import ca.hojat.notes.niki.shared.helpers.SORT_BY_NAME
import ca.hojat.notes.niki.shared.helpers.SORT_BY_SIZE
import ca.hojat.notes.niki.shared.helpers.SORT_DESCENDING
import ca.hojat.notes.niki.shared.helpers.SORT_USE_NUMERIC_VALUE
import ca.hojat.notes.niki.shared.helpers.isNougatPlus
import java.io.File

open class FileDirItem(
    val path: String,
    val name: String = "",
    var isDirectory: Boolean = false,
    var children: Int = 0,
    var size: Long = 0L,
    var modified: Long = 0L,
    var mediaStoreId: Long = 0L
) :
    Comparable<FileDirItem> {
    companion object {
        var sorting = 0
    }

    override fun toString() =
        "FileDirItem(path=$path, name=$name, isDirectory=$isDirectory, children=$children, size=$size, modified=$modified, mediaStoreId=$mediaStoreId)"

    override fun compareTo(other: FileDirItem): Int {
        return if (isDirectory && !other.isDirectory) {
            -1
        } else if (!isDirectory && other.isDirectory) {
            1
        } else {
            var result: Int
            when {
                sorting and SORT_BY_NAME != 0 -> {
                    result = if (sorting and SORT_USE_NUMERIC_VALUE != 0) {
                        AlphanumericComparator().compare(
                            name.normalizeString().lowercase(),
                            other.name.normalizeString().lowercase()
                        )
                    } else {
                        name.normalizeString().lowercase()
                            .compareTo(other.name.normalizeString().lowercase())
                    }
                }

                sorting and SORT_BY_SIZE != 0 -> result = when {
                    size == other.size -> 0
                    size > other.size -> 1
                    else -> -1
                }

                sorting and SORT_BY_DATE_MODIFIED != 0 -> {
                    result = when {
                        modified == other.modified -> 0
                        modified > other.modified -> 1
                        else -> -1
                    }
                }

                else -> {
                    result = getExtension().lowercase().compareTo(other.getExtension().lowercase())
                }
            }

            if (sorting and SORT_DESCENDING != 0) {
                result *= -1
            }
            result
        }
    }

    private fun getExtension() = if (isDirectory) name else path.substringAfterLast('.', "")

    fun getBubbleText(context: Context, dateFormat: String? = null, timeFormat: String? = null) =
        when {
            sorting and SORT_BY_SIZE != 0 -> size.formatSize()
            sorting and SORT_BY_DATE_MODIFIED != 0 -> modified.formatDate(
                context,
                dateFormat,
                timeFormat
            )

            sorting and SORT_BY_EXTENSION != 0 -> getExtension().lowercase()
            else -> name
        }

    fun getProperSize(context: Context, countHidden: Boolean): Long {
        return when {
            context.isRestrictedSAFOnlyRoot(path) -> context.getAndroidSAFFileSize(path)
            context.isPathOnOTG(path) -> context.getDocumentFile(path)?.getItemSize(countHidden)
                ?: 0

            isNougatPlus() && path.startsWith("content://") -> {
                try {
                    context.contentResolver.openInputStream(Uri.parse(path))?.available()?.toLong()
                        ?: 0L
                } catch (e: Exception) {
                    context.getSizeFromContentUri(Uri.parse(path))
                }
            }

            else -> File(path).getProperSize(countHidden)
        }
    }

    fun getParentPath() = path.getParentPath()

    private fun getSignature(): String {
        val lastModified = if (modified > 1) {
            modified
        } else {
            File(path).lastModified()
        }

        return "$path-$lastModified-$size"
    }

    fun getKey() = ObjectKey(getSignature())

    fun assembleContentUri(): Uri {
        val uri = when {
            path.isImageFast() -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            path.isVideoFast() -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Files.getContentUri("external")
        }

        return Uri.withAppendedPath(uri, mediaStoreId.toString())
    }

    fun asReadOnly() = FileDirItemReadOnly(
        path = path,
        name = name,
        isDirectory = isDirectory,
        children = children,
        size = size,
        modified = modified,
        mediaStoreId = mediaStoreId
    )

}

@Immutable
class FileDirItemReadOnly(
    path: String,
    name: String = "",
    isDirectory: Boolean = false,
    children: Int = 0,
    size: Long = 0L,
    modified: Long = 0L,
    mediaStoreId: Long = 0L
) : FileDirItem(path, name, isDirectory, children, size, modified, mediaStoreId)
