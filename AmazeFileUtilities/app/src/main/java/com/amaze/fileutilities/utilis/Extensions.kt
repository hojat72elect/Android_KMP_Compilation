package com.amaze.fileutilities.utilis

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.os.TransactionTooLargeException
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.afollestad.materialdialogs.MaterialDialog
import com.amaze.fileutilities.audio_player.AudioPlayerService
import com.amaze.fileutilities.home_page.database.BlurAnalysis
import com.amaze.fileutilities.home_page.database.BlurAnalysisDao
import com.amaze.fileutilities.home_page.database.ImageAnalysis
import com.amaze.fileutilities.home_page.database.ImageAnalysisDao
import com.amaze.fileutilities.home_page.database.InternalStorageAnalysis
import com.amaze.fileutilities.home_page.database.InternalStorageAnalysisDao
import com.amaze.fileutilities.home_page.database.LowLightAnalysis
import com.amaze.fileutilities.home_page.database.LowLightAnalysisDao
import com.amaze.fileutilities.home_page.database.MemeAnalysis
import com.amaze.fileutilities.home_page.database.MemeAnalysisDao
import com.amaze.fileutilities.home_page.database.SimilarImagesAnalysis
import com.amaze.fileutilities.home_page.database.SimilarImagesAnalysisDao
import com.amaze.fileutilities.home_page.database.SimilarImagesAnalysisMetadata
import com.amaze.fileutilities.home_page.database.SimilarImagesAnalysisMetadataDao
import com.amaze.fileutilities.utilis.dialog_picker.FileFilter
import com.amaze.fileutilities.utilis.dialog_picker.fileChooser
import com.amaze.fileutilities.utilis.dialog_picker.folderChooser
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

var log: Logger = LoggerFactory.getLogger(Utils::class.java)

fun Uri.getSiblingUriFiles(filter: (File) -> Boolean): ArrayList<Uri>? {
    try {
        val currentPath = getFileFromUri()
        currentPath?.let {
            if (currentPath.exists()) {
                val parent = currentPath.parentFile
                val siblings: ArrayList<Uri>?
                if (parent != null) {
                    val filesList = parent.listFiles()
                    if (filesList != null) {
                        val filteredFiles = filesList.filter(filter)
                        if (filteredFiles.size > 1000) {
                            siblings = arrayListOf(this)
                        } else {
                            siblings = ArrayList()
                            filteredFiles.sortedBy { it.lastModified() }.forEach { currentSibling ->
                                siblings.add(
                                    Uri.parse(
                                        if (!currentSibling.path
                                                .startsWith("/")
                                        )
                                            "/${currentSibling.path}"
                                        else currentSibling.path
                                    )
                                )
                            }
                        }
                    } else {
                        siblings = arrayListOf(this)
                    }
                } else {
                    siblings = arrayListOf(this)
                }
                return siblings
            }
        }
    } catch (exception: Exception) {
        log.warn("Failed to get siblings", exception)
        return null
    }
    return null
}

fun Uri.getDocumentFileFromUri(context: Context): DocumentFile? {
    if (this == Uri.EMPTY) {
        return null
    }
    try {
        val documentFile = DocumentFile.fromSingleUri(
            context,
            this
        )
        if (documentFile?.exists() == true) {
            return documentFile
        }
    } catch (e: Exception) {
        log.warn("failed to get document file from single uri", e)
    }
    try {
        val treeDocumentFile = DocumentFile.fromTreeUri(context, this)
        if (treeDocumentFile?.exists() == true) {
            return treeDocumentFile
        }
    } catch (e: Exception) {
        log.warn("failed to get document file from tree uri", e)
    }
    return null
}

fun Uri.getFileFromUri(context: Context): File? {
    if (this == Uri.EMPTY) {
        return null
    }
    var songFile: File? = getFileFromUri()
    if (songFile == null) {
        songFile = getContentResolverFilePathFromUri(context, this)?.let { filePath ->
            File(filePath)
        }
        if (songFile == null) {
            var parcelFileDescriptor: ParcelFileDescriptor? = null
            var outputStream: FileOutputStream? = null
            try {
                parcelFileDescriptor = context.contentResolver.openFileDescriptor(
                    this,
                    "r"
                )
                parcelFileDescriptor?.let {
                    val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
                    if (fileDescriptor.valid()) {
                        val fis = FileInputStream(fileDescriptor)
                        val outputFile = File(
                            context.cacheDir,
                            getContentName(context.contentResolver) ?: "sharedFile"
                        )
                        outputStream = outputFile.outputStream()
                        outputStream?.let {
                            fis.copyTo(it)
                        }
                        songFile = outputFile
                    }
                }
            } catch (e: Exception) {
                log.warn("failed to find file from uri {}", e)
            } finally {
                parcelFileDescriptor?.close()
                outputStream?.close()
            }
        }
    }
    return songFile
}

fun Uri.getContentName(resolver: ContentResolver): String? {
    var cursor: Cursor? = null
    try {
        cursor =
            resolver.query(
                this, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                null,
                null, null
            )
        cursor!!.moveToFirst()
        val nameIndex = cursor.getColumnIndex(cursor.columnNames[0])
        return if (nameIndex >= 0) {
            cursor.getString(nameIndex)
        } else {
            null
        }
    } catch (e: Exception) {
        log.warn("failed to load name for uri {}", this)
        return null
    } finally {
        cursor?.close()
    }
}

fun Uri.getFileFromUri(): File? {
    if (this == Uri.EMPTY) {
        return null
    }
    var songFile: File? = null
    if (this.authority != null && this.authority == "com.android.externalstorage.documents") {
        songFile = File(
            Environment.getExternalStorageDirectory(),
            this.path!!.split(":".toRegex(), 2).toTypedArray()[1]
        )
    }
    /*if (songFile == null) {
        val path: String? = getContentResolverFilePathFromUri(context, this)
        if (path != null) songFile = File(path)
    }*/
    if ((songFile == null || !songFile.exists()) && this.path != null) {
        songFile = File(
            this.path?.substring(
                this.path?.indexOf("/", 1)!! + 1
            )!!
        )
        if (!songFile.exists()) {
            songFile = this.path?.let { File(it) }
        }
    }
    if (songFile == null || !songFile.exists()) {
        return null
    }
    return songFile
}

private fun getContentResolverFilePathFromUri(context: Context, uri: Uri): String? {
    var cursor: Cursor? = null
    try {
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
        cursor = context
            .contentResolver
            .query(
                uri,
                projection, null,
                null,
                null
            )
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            return cursor.getString(columnIndex)
        }
    } catch (e: Exception) {
        log.warn("failed to get cursor resolver from task", e)
    } finally {
        cursor?.close()
    }
    return null
}

fun File.isImageMimeType(): Boolean {
    return this.path.endsWith("jpg") ||
            this.path.endsWith("jpe") ||
            this.path.endsWith("jpeg") ||
            this.path.endsWith("jfif") ||
            this.path.endsWith("pjpeg") ||
            this.path.endsWith("pjp") ||
            this.path.endsWith("gif") ||
            this.path.endsWith("png") ||
            this.path.endsWith("svg") ||
            this.path.endsWith("webp")
}

fun File.isAudioMimeType(): Boolean {
    return this.path.endsWith("mp3") ||
            this.path.endsWith("wav") ||
            this.path.endsWith("ogg") ||
            this.path.endsWith("mp4") ||
            this.path.endsWith("m4a") ||
            this.path.endsWith("fmp4") ||
            this.path.endsWith("flv") ||
            this.path.endsWith("flac") ||
            this.path.endsWith("amr") ||
            this.path.endsWith("aac") ||
            this.path.endsWith("ac3") ||
            this.path.endsWith("eac3") ||
            this.path.endsWith("dca") ||
            this.path.endsWith("opus")
}

val Int.px get() = this * Resources.getSystem().displayMetrics.density
val Float.px get() = this * Resources.getSystem().displayMetrics.density

/**
 * Allow null checks on more than one parameters at the same time.
 * Alternative of doing nested p1?.let p2?.let
 */
inline fun <T1 : Any, T2 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    block: (T1, T2) -> R?
): R? {
    return if (p1 != null && p2 != null) block(
        p1,
        p2
    ) else null
}

fun Context.showToastInCenter(message: String): Toast = Toast.makeText(
    this,
    message, Toast.LENGTH_SHORT
)
    .apply { setGravity(Gravity.CENTER, 0, 0); show() }

fun Context.showToastOnBottom(message: String): Toast = Toast.makeText(
    this,
    message, Toast.LENGTH_SHORT
)
    .apply { setGravity(Gravity.BOTTOM, 0, 0); show() }

fun View.hideFade(duration: Long) {
    this.animate().alpha(0f).duration = duration
    this.visibility = View.GONE
}

fun View.showFade(duration: Long) {
    this.animate().alpha(1f).duration = duration
    this.visibility = View.VISIBLE
}

fun View.hideTranslateY(duration: Long) {
    val animation: Animation = TranslateAnimation(0f, 0f, 0f, this.y)
    animation.duration = duration
//    animation.fillAfter = true
    this.startAnimation(animation)
    this.visibility = View.GONE
}

fun View.showTranslateY(duration: Long) {
    val animation: Animation = TranslateAnimation(0f, 0f, this.y, 0f)
    animation.duration = duration
//    animation.fillAfter = true
    this.startAnimation(animation)
    this.visibility = View.VISIBLE
}

fun Context.getAppCommonSharedPreferences(): SharedPreferences {
    return this.getSharedPreferences(
        PreferencesConstants.PREFERENCE_FILE,
        Context.MODE_PRIVATE
    )
}

fun Context.startServiceSafely(intent: Intent, extraName: String) {
    try {
        this.startService(intent)
    } catch (ttle: TransactionTooLargeException) {
        // decrease by 10% and try recursively
        val intentUriList: ArrayList<Uri>? = intent.getParcelableArrayListExtra(extraName)
        intentUriList?.let {
            if (intentUriList.isNotEmpty()) {
                log.warn("failed to start service with extras size ${intentUriList.size}", ttle)
                val uri = it.take(intentUriList.size - (intentUriList.size / 10))
                if (uri.isNotEmpty()) {
                    log.warn("trying to start with new size ${uri.size}")
                    intent.putParcelableArrayListExtra(
                        AudioPlayerService.ARG_URI_LIST,
                        ArrayList(uri)
                    )
                    startServiceSafely(intent, extraName)
                } else {
                    log.error("couldn't start service safely, returning...", ttle)
                    return
                }
            } else {
                log.error("couldn't start service safely, returning...", ttle)
                return
            }
        }
    }
}

fun ImageAnalysis.invalidate(dao: ImageAnalysisDao): Boolean {
    val file = File(filePath)
    return if (!file.exists()) {
        dao.delete(this)
        false
    } else {
        true
    }
}

fun BlurAnalysis.invalidate(dao: BlurAnalysisDao): Boolean {
    val file = File(filePath)
    return if (!file.exists()) {
        dao.delete(this)
        false
    } else {
        true
    }
}

fun MemeAnalysis.invalidate(dao: MemeAnalysisDao): Boolean {
    val file = File(filePath)
    return if (!file.exists()) {
        dao.delete(this)
        false
    } else {
        true
    }
}

fun LowLightAnalysis.invalidate(dao: LowLightAnalysisDao): Boolean {
    val file = File(filePath)
    return if (!file.exists()) {
        dao.delete(this)
        false
    } else {
        true
    }
}

fun SimilarImagesAnalysisMetadata.invalidate(dao: SimilarImagesAnalysisMetadataDao): Boolean {
    val file = File(filePath)
    val parentFile = File(parentPath)
    return if (!parentFile.exists() || !file.exists()) {
        dao.delete(this)
        false
    } else {
        true
    }
}

fun SimilarImagesAnalysis.invalidate(dao: SimilarImagesAnalysisDao): Boolean {
    val validFiles = mutableSetOf<String>()
    for (imageFilePath in files) {
        val imageFile = File(imageFilePath)
        if (imageFile.exists()) {
            validFiles.add(imageFilePath)
        }
    }
    if (validFiles.size <= 1) {
        dao.delete(this)
        return false
    } else if (validFiles.size != files.size) {
        this.files = validFiles
        dao.insert(this)
    }
    return true
}

fun InternalStorageAnalysis.invalidate(dao: InternalStorageAnalysisDao): Boolean {
    val validFiles = mutableListOf<String>()
    for (imageFilePath in files) {
        val imageFile = File(imageFilePath)
        if (imageFile.exists()) {
            validFiles.add(imageFilePath)
        }
    }
    if (validFiles.size <= 1) {
        dao.delete(this)
        return false
    } else if (validFiles.size != files.size) {
        this.files = validFiles
        dao.insert(this)
    }
    return true
}

fun Context.getExternalStorageDirectory(): StorageDirectoryParcelable? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileUtils.getStorageDirectoriesNew(applicationContext.applicationContext)
    } else {
        FileUtils.getStorageDirectoriesLegacy(applicationContext.applicationContext)
    }
}

fun Context.showFolderChooserDialog(chooserPath: (file: File) -> Unit) {
    val initialFolder = getExternalStorageDirectory()
    initialFolder?.let {
        val baseFile = File(it.path)
        MaterialDialog(this).show {
            folderChooser(
                this@showFolderChooserDialog,
                baseFile,
            ) { dialog, folder ->
                chooserPath.invoke(folder)
                dialog.dismiss()
            }
        }
    }
}

fun Context.showFileChooserDialog(filter: FileFilter = null, chooserPath: (file: File) -> Unit) {
    val initialFolder = getExternalStorageDirectory()
    initialFolder?.let {
        val baseFile = File(it.path)
        MaterialDialog(this).show {
            fileChooser(
                this@showFileChooserDialog,
                baseFile,
                filter
            ) { dialog, folder ->
                chooserPath.invoke(folder)
                dialog.dismiss()
            }
        }
    }
}

fun String.removeExtension(): String {
    return this.substring(0, this.lastIndexOf("."))
}

fun Context.isNetworkAvailable(): Boolean {
    log.info("fetching network connection")
    val connectivityManager = getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE)
            as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun String.doesFileExist(): Boolean {
    return File(this).exists()
}

fun <P, R> CoroutineScope.executeAsyncTask(
    onPreExecute: () -> Unit,
    doInBackground: suspend (suspend (P) -> Unit) -> R,
    onPostExecute: (R) -> Unit,
    onProgressUpdate: (P) -> Unit
) = launch {
    onPreExecute()

    val result = withContext(Dispatchers.IO) {
        doInBackground {
            withContext(Dispatchers.Main) { onProgressUpdate(it) }
        }
    }
    onPostExecute(result)
}

fun TextView.setTextAnimation(
    text: String,
    duration: Long = 300,
    completion: (() -> Unit)? = null
) {
    fadOutAnimation(duration) {
        this.text = text
        fadInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun View.fadOutAnimation(
    duration: Long = 300,
    visibility: Int = View.INVISIBLE,
    completion: (() -> Unit)? = null
) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }
}

fun View.fadInAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}

fun Context.getScreenBrightness(): Float {
    val brightness: Int
    try {
        val contentResolver = this.contentResolver
        brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    } catch (e: Exception) {
        log.warn("failed to get system brightness", e)
        return -1f
    }
    return (brightness / 255).toFloat()
}
