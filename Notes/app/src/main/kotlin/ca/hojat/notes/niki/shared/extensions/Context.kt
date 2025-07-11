package ca.hojat.notes.niki.shared.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ShortcutManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Color
import android.graphics.Point
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Document
import android.provider.MediaStore
import android.provider.MediaStore.Audio
import android.provider.MediaStore.Files
import android.provider.MediaStore.Images
import android.provider.MediaStore.MediaColumns
import android.provider.MediaStore.Video
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.loader.content.CursorLoader
import com.github.ajalt.reprint.core.Reprint
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.dialogs.UnlockNotesDialog
import ca.hojat.notes.niki.shared.interfaces.NotesDao
import ca.hojat.notes.niki.shared.interfaces.WidgetsDao
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.shared.data.databases.NotesDatabase
import ca.hojat.notes.niki.shared.data.models.FileDirItem
import ca.hojat.notes.niki.shared.data.models.Note
import ca.hojat.notes.niki.shared.data.models.SharedTheme
import ca.hojat.notes.niki.shared.helpers.AUTOMATIC_BACKUP_REQUEST_CODE
import ca.hojat.notes.niki.shared.helpers.BaseConfig
import ca.hojat.notes.niki.shared.helpers.Config
import ca.hojat.notes.niki.shared.helpers.DARK_GREY
import ca.hojat.notes.niki.shared.helpers.EXTERNAL_STORAGE_PROVIDER_AUTHORITY
import ca.hojat.notes.niki.shared.helpers.ExportResult
import ca.hojat.notes.niki.shared.helpers.ExternalStorageProviderHack
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_LARGE
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_MEDIUM
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_SMALL
import ca.hojat.notes.niki.shared.helpers.MyContentProvider
import ca.hojat.notes.niki.shared.helpers.MyWidgetProvider
import ca.hojat.notes.niki.shared.helpers.NotesHelper
import ca.hojat.notes.niki.shared.helpers.PERMISSION_ACCESS_COARSE_LOCATION
import ca.hojat.notes.niki.shared.helpers.PERMISSION_ACCESS_FINE_LOCATION
import ca.hojat.notes.niki.shared.helpers.PERMISSION_CALL_PHONE
import ca.hojat.notes.niki.shared.helpers.PERMISSION_CAMERA
import ca.hojat.notes.niki.shared.helpers.PERMISSION_GET_ACCOUNTS
import ca.hojat.notes.niki.shared.helpers.PERMISSION_MEDIA_LOCATION
import ca.hojat.notes.niki.shared.helpers.PERMISSION_POST_NOTIFICATIONS
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_CALENDAR
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_CALL_LOG
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_CONTACTS
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_MEDIA_AUDIO
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_MEDIA_IMAGES
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_MEDIA_VIDEO
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_MEDIA_VISUAL_USER_SELECTED
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_PHONE_STATE
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_SMS
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_STORAGE
import ca.hojat.notes.niki.shared.helpers.PERMISSION_READ_SYNC_SETTINGS
import ca.hojat.notes.niki.shared.helpers.PERMISSION_RECORD_AUDIO
import ca.hojat.notes.niki.shared.helpers.PERMISSION_SEND_SMS
import ca.hojat.notes.niki.shared.helpers.PERMISSION_WRITE_CALENDAR
import ca.hojat.notes.niki.shared.helpers.PERMISSION_WRITE_CALL_LOG
import ca.hojat.notes.niki.shared.helpers.PERMISSION_WRITE_CONTACTS
import ca.hojat.notes.niki.shared.helpers.PERMISSION_WRITE_STORAGE
import ca.hojat.notes.niki.shared.helpers.PREFS_KEY
import ca.hojat.notes.niki.shared.helpers.SD_OTG_PATTERN
import ca.hojat.notes.niki.shared.helpers.SD_OTG_SHORT
import ca.hojat.notes.niki.shared.helpers.TIME_FORMAT_12
import ca.hojat.notes.niki.shared.helpers.TIME_FORMAT_24
import ca.hojat.notes.niki.shared.helpers.appIconColorStrings
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.shared.helpers.getNextAutoBackupTime
import ca.hojat.notes.niki.shared.helpers.getPreviousAutoBackupTime
import ca.hojat.notes.niki.shared.helpers.isOnMainThread
import ca.hojat.notes.niki.shared.helpers.isOreoPlus
import ca.hojat.notes.niki.shared.helpers.isQPlus
import ca.hojat.notes.niki.shared.helpers.isRPlus
import ca.hojat.notes.niki.shared.helpers.isSPlus
import ca.hojat.notes.niki.shared.helpers.proPackages
import ca.hojat.notes.niki.shared.receivers.AutomaticBackupReceiver
import ca.hojat.notes.niki.shared.views.MyAppCompatCheckbox
import ca.hojat.notes.niki.shared.views.MyAppCompatSpinner
import ca.hojat.notes.niki.shared.views.MyAutoCompleteTextView
import ca.hojat.notes.niki.shared.views.MyButton
import ca.hojat.notes.niki.shared.views.MyCompatRadioButton
import ca.hojat.notes.niki.shared.views.MyEditText
import ca.hojat.notes.niki.shared.views.MyFloatingActionButton
import ca.hojat.notes.niki.shared.views.MySeekBar
import ca.hojat.notes.niki.shared.views.MyTextInputLayout
import ca.hojat.notes.niki.shared.views.MyTextView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern
import org.joda.time.DateTime

private const val ANDROID_DATA_DIR = "/Android/data/"
private const val ANDROID_OBB_DIR = "/Android/obb/"
val Context.recycleBinPath: String get() = filesDir.absolutePath
val DIRS_ACCESSIBLE_ONLY_WITH_SAF = listOf(ANDROID_DATA_DIR, ANDROID_OBB_DIR)


val Context.config: Config get() = Config.newInstance(applicationContext)

fun Context.createAndroidDataOrObbPath(fullPath: String): String {
    return if (isAndroidDataDir(fullPath)) {
        fullPath.getBasePath(this).trimEnd('/').plus(ANDROID_DATA_DIR)
    } else {
        fullPath.getBasePath(this).trimEnd('/').plus(ANDROID_OBB_DIR)
    }
}

val Context.notesDB: NotesDao get() = NotesDatabase.getInstance(applicationContext).NotesDao()

val Context.widgetsDB: WidgetsDao get() = NotesDatabase.getInstance(applicationContext).WidgetsDao()

fun Context.updateWidgets() {
    val widgetIDs = AppWidgetManager.getInstance(applicationContext)
        ?.getAppWidgetIds(ComponentName(applicationContext, MyWidgetProvider::class.java)) ?: return
    if (widgetIDs.isNotEmpty()) {
        Intent(applicationContext, MyWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIDs)
            sendBroadcast(this)
        }
    }
}

fun Context.isRestrictedSAFOnlyRoot(path: String): Boolean {
    return isRPlus() && isSAFOnlyRoot(path)
}

fun Context.getDocumentFile(path: String): DocumentFile? {
    val isOTG = isPathOnOTG(path)
    var relativePath = path.substring(if (isOTG) otgPath.length else sdCardPath.length)
    if (relativePath.startsWith(File.separator)) {
        relativePath = relativePath.substring(1)
    }

    return try {
        val treeUri = Uri.parse(if (isOTG) baseConfig.otgTreeUri else baseConfig.sdTreeUri)
        var document = DocumentFile.fromTreeUri(applicationContext, treeUri)
        val parts = relativePath.split("/").filter { it.isNotEmpty() }
        for (part in parts) {
            document = document?.findFile(part)
        }
        document
    } catch (ignored: Exception) {
        null
    }
}

// these functions update the mediastore instantly, MediaScannerConnection.scanFileRecursively takes some time to really get applied
fun Context.deleteFromMediaStore(path: String, callback: ((needsRescan: Boolean) -> Unit)? = null) {
    if (getIsPathDirectory(path)) {
        callback?.invoke(false)
        return
    }

    ensureBackgroundThread {
        try {
            val where = "${MediaColumns.DATA} = ?"
            val args = arrayOf(path)
            val needsRescan = contentResolver.delete(getFileUri(path), where, args) != 1
            callback?.invoke(needsRescan)
        } catch (ignored: Exception) {
            callback?.invoke(true)
        }
    }
}

fun Context.isPathOnSD(path: String) = sdCardPath.isNotEmpty() && path.startsWith(sdCardPath)

fun Context.isPathOnOTG(path: String) = otgPath.isNotEmpty() && path.startsWith(otgPath)

fun Context.isBiometricIdAvailable(): Boolean = when (BiometricManager.from(this).canAuthenticate(
    BiometricManager.Authenticators.BIOMETRIC_WEAK
)) {
    BiometricManager.BIOMETRIC_SUCCESS, BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> true
    else -> false
}

fun Context.getPercentageFontSize() =
    resources.getDimension(R.dimen.middle_text_size) * (config.fontSizePercentage / 100f)

fun BaseActivity.requestUnlockNotes(
    notes: List<Note>,
    callback: (unlockedNotes: List<Note>) -> Unit
) {
    val lockedNotes = notes.filter { it.isLocked() }
    if (lockedNotes.isNotEmpty()) {
        runOnUiThread {
            UnlockNotesDialog(this, lockedNotes, callback)
        }
    } else {
        callback(emptyList())
    }
}

/**
 * Fires an intent to automatically backup notes.
 */
fun Context.getAutomaticBackupIntent(): PendingIntent {
    val intent = Intent(this, AutomaticBackupReceiver::class.java)
    return PendingIntent.getBroadcast(
        this,
        AUTOMATIC_BACKUP_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

fun Context.scheduleNextAutomaticBackup() {
    if (config.autoBackup) {
        val backupAtMillis = getNextAutoBackupTime().millis
        val pendingIntent = getAutomaticBackupIntent()
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        try {
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                backupAtMillis,
                pendingIntent
            )
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }
}

fun Context.cancelScheduledAutomaticBackup() {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(getAutomaticBackupIntent())
}

fun Context.checkAndBackupNotesOnBoot() {
    if (config.autoBackup) {
        val previousRealBackupTime = config.lastAutoBackupTime
        val previousScheduledBackupTime = getPreviousAutoBackupTime().millis
        val missedPreviousBackup = previousRealBackupTime < previousScheduledBackupTime
        if (missedPreviousBackup) {
            // device was probably off at the scheduled time so backup now
            backupNotes()
        }
    }
}

/**
 * This function is used to save the notes to the device. It is called by the `AutomaticBackupReceiver`.
 */
fun Context.backupNotes() {
    require(isRPlus())
    ensureBackgroundThread {
        val config = config
        NotesHelper(this).getNotes { notesToBackup ->
            if (notesToBackup.isEmpty()) {
                toast(R.string.no_entries_for_exporting)
                config.lastAutoBackupTime = DateTime.now().millis
                scheduleNextAutomaticBackup()
                return@getNotes
            }


            val now = DateTime.now()
            val year = now.year.toString()
            val month = now.monthOfYear.ensureTwoDigits()
            val day = now.dayOfMonth.ensureTwoDigits()
            val hours = now.hourOfDay.ensureTwoDigits()
            val minutes = now.minuteOfHour.ensureTwoDigits()
            val seconds = now.secondOfMinute.ensureTwoDigits()

            val filename = config.autoBackupFilename
                .replace("%Y", year, false)
                .replace("%M", month, false)
                .replace("%D", day, false)
                .replace("%h", hours, false)
                .replace("%m", minutes, false)
                .replace("%s", seconds, false)

            val outputFolder = File(config.autoBackupFolder).apply {
                mkdirs()
            }

            var exportFile = File(outputFolder, "$filename.json")
            var exportFilePath = exportFile.absolutePath
            val outputStream = try {
                if (hasProperStoredFirstParentUri(exportFilePath)) {
                    val exportFileUri = createDocumentUriUsingFirstParentTreeUri(exportFilePath)
                    if (!getDoesFilePathExist(exportFilePath)) {
                        createSAFFileSdk30(exportFilePath)
                    }
                    applicationContext.contentResolver.openOutputStream(exportFileUri, "wt")
                        ?: FileOutputStream(exportFile)
                } else {
                    var num = 0
                    while (getDoesFilePathExist(exportFilePath) && !exportFile.canWrite()) {
                        num++
                        exportFile = File(outputFolder, "${filename}_${num}.json")
                        exportFilePath = exportFile.absolutePath
                    }
                    FileOutputStream(exportFile)
                }
            } catch (e: Exception) {
                showErrorToast(e)
                scheduleNextAutomaticBackup()
                return@getNotes
            }

            val exportResult = try {
                NotesHelper(this).exportNotes(notesToBackup, outputStream)
            } catch (e: Exception) {
                showErrorToast(e)
            }

            if (exportResult == ExportResult.EXPORT_FAIL) {
                toast(R.string.exporting_failed)
            }

            config.lastAutoBackupTime = DateTime.now().millis
            scheduleNextAutomaticBackup()
        }
    }
}

private const val DOWNLOAD_DIR = "Download"
private const val ANDROID_DIR = "Android"
private val DIRS_INACCESSIBLE_WITH_SAF_SDK_30 = listOf(DOWNLOAD_DIR, ANDROID_DIR)

fun Context.hasProperStoredFirstParentUri(path: String): Boolean {
    val firstParentUri = createFirstParentTreeUri(path)
    return contentResolver.persistedUriPermissions.any { it.uri.toString() == firstParentUri.toString() }
}

fun Context.isAccessibleWithSAFSdk30(path: String): Boolean {
    if (path.startsWith(recycleBinPath) || isExternalStorageManager()) {
        return false
    }

    val level = getFirstParentLevel(path)
    val firstParentDir = path.getFirstParentDirName(this, level)
    val firstParentPath = path.getFirstParentPath(this, level)

    val isValidName = firstParentDir != null
    val isDirectory = File(firstParentPath).isDirectory
    val isAnAccessibleDirectory =
        DIRS_INACCESSIBLE_WITH_SAF_SDK_30.all { !firstParentDir.equals(it, true) }
    return isRPlus() && isValidName && isDirectory && isAnAccessibleDirectory
}

fun Context.getFirstParentLevel(path: String): Int {
    return when {
        isRPlus() && (isInAndroidDir(path) || isInSubFolderInDownloadDir(path)) -> 1
        else -> 0
    }
}

fun Context.isRestrictedWithSAFSdk30(path: String): Boolean {
    if (path.startsWith(recycleBinPath) || isExternalStorageManager()) {
        return false
    }

    val level = getFirstParentLevel(path)
    val firstParentDir = path.getFirstParentDirName(this, level)
    val firstParentPath = path.getFirstParentPath(this, level)

    val isInvalidName = firstParentDir == null
    val isDirectory = File(firstParentPath).isDirectory
    val isARestrictedDirectory =
        DIRS_INACCESSIBLE_WITH_SAF_SDK_30.any { firstParentDir.equals(it, true) }
    return isRPlus() && (isInvalidName || (isDirectory && isARestrictedDirectory))
}

fun Context.isInDownloadDir(path: String): Boolean {
    if (path.startsWith(recycleBinPath)) {
        return false
    }
    val firstParentDir = path.getFirstParentDirName(this, 0)
    return firstParentDir.equals(DOWNLOAD_DIR, true)
}

fun Context.isInSubFolderInDownloadDir(path: String): Boolean {
    if (path.startsWith(recycleBinPath)) {
        return false
    }
    val firstParentDir = path.getFirstParentDirName(this, 1)
    return if (firstParentDir == null) {
        false
    } else {
        val startsWithDownloadDir = firstParentDir.startsWith(DOWNLOAD_DIR, true)
        val hasAtLeast1PathSegment = firstParentDir.split("/").filter { it.isNotEmpty() }.size > 1
        val firstParentPath = path.getFirstParentPath(this, 1)
        startsWithDownloadDir && hasAtLeast1PathSegment && File(firstParentPath).isDirectory
    }
}

fun Context.isInAndroidDir(path: String): Boolean {
    if (path.startsWith(recycleBinPath)) {
        return false
    }
    val firstParentDir = path.getFirstParentDirName(this, 0)
    return firstParentDir.equals(ANDROID_DIR, true)
}

fun isExternalStorageManager(): Boolean {
    return isRPlus() && Environment.isExternalStorageManager()
}

// is the app a Media Management App on Android 12+?
fun Context.canManageMedia(): Boolean {
    return isSPlus() && MediaStore.canManageMedia(this)
}

fun Context.createFirstParentTreeUriUsingRootTree(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)
    val level = getFirstParentLevel(fullPath)
    val rootParentDirName = fullPath.getFirstParentDirName(this, level)
    val treeUri =
        DocumentsContract.buildTreeDocumentUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, "$storageId:")
    val documentId = "${storageId}:$rootParentDirName"
    return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
}

fun Context.getSAFOnlyDirs(): List<String> {
    return DIRS_ACCESSIBLE_ONLY_WITH_SAF.map { "$internalStoragePath$it" } +
            DIRS_ACCESSIBLE_ONLY_WITH_SAF.map { "$sdCardPath$it" }
}

fun Context.createFirstParentTreeUri(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)
    val level = getFirstParentLevel(fullPath)
    val rootParentDirName = fullPath.getFirstParentDirName(this, level)
    val firstParentId = "$storageId:$rootParentDirName"
    return DocumentsContract.buildTreeDocumentUri(
        EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
        firstParentId
    )
}

fun Context.createDocumentUriUsingFirstParentTreeUri(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)
    val relativePath = when {
        fullPath.startsWith(internalStoragePath) -> fullPath.substring(internalStoragePath.length)
            .trim('/')

        else -> fullPath.substringAfter(storageId).trim('/')
    }
    val treeUri = createFirstParentTreeUri(fullPath)
    val documentId = "${storageId}:$relativePath"
    return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
}

fun Context.getSAFDocumentId(path: String): String {
    val basePath = path.getBasePath(this)
    val relativePath = path.substring(basePath.length).trim('/')
    val storageId = getSAFStorageId(path)
    return "$storageId:$relativePath"
}

fun Context.createDocumentUriFromRootTree(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)

    val relativePath = when {
        fullPath.startsWith(internalStoragePath) -> fullPath.substring(internalStoragePath.length)
            .trim('/')

        else -> fullPath.substringAfter(storageId).trim('/')
    }

    val treeUri =
        DocumentsContract.buildTreeDocumentUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, "$storageId:")
    val documentId = "${storageId}:$relativePath"
    return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
}

fun Context.getHumanReadablePath(path: String): String {
    return getString(
        when (path) {
            "/" -> R.string.root
            internalStoragePath -> R.string.internal
            otgPath -> R.string.usb
            else -> R.string.sd_card
        }
    )
}

fun Context.createSAFDirectorySdk30(path: String): Boolean {
    return try {
        val treeUri = createFirstParentTreeUri(path)
        val parentPath = path.getParentPath()
        if (!getDoesFilePathExistSdk30(parentPath)) {
            createSAFDirectorySdk30(parentPath)
        }

        val documentId = getSAFDocumentId(parentPath)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.createDocument(
            contentResolver,
            parentUri,
            Document.MIME_TYPE_DIR,
            path.getFilenameFromPath()
        ) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.createAndroidSAFDocumentId(path: String): String {
    val basePath = path.getBasePath(this)
    val relativePath = path.substring(basePath.length).trim('/')
    val storageId = getStorageRootIdForAndroidDir(path)
    return "$storageId:$relativePath"
}

fun Context.createSAFFileSdk30(path: String): Boolean {
    return try {
        val treeUri = createFirstParentTreeUri(path)
        val parentPath = path.getParentPath()
        if (!getDoesFilePathExistSdk30(parentPath)) {
            createSAFDirectorySdk30(parentPath)
        }

        val documentId = getSAFDocumentId(parentPath)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.createDocument(
            contentResolver,
            parentUri,
            path.getMimeType(),
            path.getFilenameFromPath()
        ) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.getDoesFilePathExistSdk30(path: String): Boolean {
    return when {
        isAccessibleWithSAFSdk30(path) -> getFastDocumentSdk30(path)?.exists() ?: false
        else -> File(path).exists()
    }
}

fun Context.getSomeDocumentSdk30(path: String): DocumentFile? =
    getFastDocumentSdk30(path) ?: getDocumentSdk30(path)

fun Context.getFastDocumentSdk30(path: String): DocumentFile? {
    val uri = createDocumentUriUsingFirstParentTreeUri(path)
    return DocumentFile.fromSingleUri(this, uri)
}

fun Context.getDocumentSdk30(path: String): DocumentFile? {
    val level = getFirstParentLevel(path)
    val firstParentPath = path.getFirstParentPath(this, level)
    var relativePath = path.substring(firstParentPath.length)
    if (relativePath.startsWith(File.separator)) {
        relativePath = relativePath.substring(1)
    }

    return try {
        val treeUri = createFirstParentTreeUri(path)
        var document = DocumentFile.fromTreeUri(applicationContext, treeUri)
        val parts = relativePath.split("/").filter { it.isNotEmpty() }
        for (part in parts) {
            document = document?.findFile(part)
        }
        document
    } catch (ignored: Exception) {
        null
    }
}

fun Context.deleteDocumentWithSAFSdk30(
    fileDirItem: FileDirItem,
    allowDeleteFolder: Boolean,
    callback: ((wasSuccess: Boolean) -> Unit)?
) {
    try {
        var fileDeleted = false
        if (fileDirItem.isDirectory.not() || allowDeleteFolder) {
            val fileUri = createDocumentUriUsingFirstParentTreeUri(fileDirItem.path)
            fileDeleted = DocumentsContract.deleteDocument(contentResolver, fileUri)
        }

        if (fileDeleted) {
            deleteFromMediaStore(fileDirItem.path)
            callback?.invoke(true)
        }

    } catch (e: Exception) {
        callback?.invoke(false)
        showErrorToast(e)
    }
}

fun Context.renameDocumentSdk30(oldPath: String, newPath: String): Boolean {
    return try {
        val treeUri = createFirstParentTreeUri(oldPath)
        val documentId = getSAFDocumentId(oldPath)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.renameDocument(
            contentResolver,
            parentUri,
            newPath.getFilenameFromPath()
        ) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.hasProperStoredDocumentUriSdk30(path: String): Boolean {
    val documentUri = buildDocumentUriSdk30(path)
    return contentResolver.persistedUriPermissions.any { it.uri.toString() == documentUri.toString() }
}

fun Context.buildDocumentUriSdk30(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)

    val relativePath = when {
        fullPath.startsWith(internalStoragePath) -> fullPath.substring(internalStoragePath.length)
            .trim('/')

        else -> fullPath.substringAfter(storageId).trim('/')
    }

    val documentId = "${storageId}:$relativePath"
    return DocumentsContract.buildDocumentUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, documentId)
}


fun Context.getSharedPrefs(): SharedPreferences =
    getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

val Context.isRTLLayout: Boolean get() = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

val Context.areSystemAnimationsEnabled: Boolean
    get() = Settings.Global.getFloat(
        contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        0f
    ) > 0f

fun Context.toast(id: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), length)
}

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    try {
        if (isOnMainThread()) {
            doToast(this, msg, length)
        } else {
            Handler(Looper.getMainLooper()).post {
                doToast(this, msg, length)
            }
        }
    } catch (_: Exception) {
    }
}

fun Context.hasProperStoredAndroidTreeUri(path: String): Boolean {
    val uri = getAndroidTreeUri(path)
    val hasProperUri = contentResolver.persistedUriPermissions.any { it.uri.toString() == uri }
    if (!hasProperUri) {
        storeAndroidTreeUri(path, "")
    }
    return hasProperUri
}

fun Context.humanizePath(path: String): String {
    val trimmedPath = path.trimEnd('/')
    return when (val basePath = path.getBasePath(this)) {
        "/" -> "${getHumanReadablePath(basePath)}$trimmedPath"
        else -> trimmedPath.replaceFirst(basePath, getHumanReadablePath(basePath))
    }
}

fun Context.hasProperStoredTreeUri(isOTG: Boolean): Boolean {
    val uri = if (isOTG) baseConfig.otgTreeUri else baseConfig.sdTreeUri
    val hasProperUri = contentResolver.persistedUriPermissions.any { it.uri.toString() == uri }
    if (!hasProperUri) {
        if (isOTG) {
            baseConfig.otgTreeUri = ""
        } else {
            baseConfig.sdTreeUri = ""
        }
    }
    return hasProperUri
}

private fun doToast(context: Context, message: String, length: Int) {
    if (context is Activity) {
        if (!context.isFinishing && !context.isDestroyed) {
            Toast.makeText(context, message, length).show()
        }
    } else {
        Toast.makeText(context, message, length).show()
    }
}

fun Context.showErrorToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    toast(String.format(getString(R.string.error), msg), length)
}

fun Context.showErrorToast(exception: Exception, length: Int = Toast.LENGTH_LONG) {
    showErrorToast(exception.toString(), length)
}

fun Context.createAndroidSAFFile(path: String): Boolean {
    return try {
        val treeUri = getAndroidTreeUri(path).toUri()
        val parentPath = path.getParentPath()
        if (!getDoesFilePathExist(parentPath)) {
            createAndroidSAFDirectory(parentPath)
        }

        val documentId = createAndroidSAFDocumentId(path.getParentPath())
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.createDocument(
            contentResolver,
            parentUri,
            path.getMimeType(),
            path.getFilenameFromPath()
        ) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.createAndroidDataOrObbUri(fullPath: String): Uri {
    val path = createAndroidDataOrObbPath(fullPath)
    return createDocumentUriFromRootTree(path)
}

val Context.baseConfig: BaseConfig get() = BaseConfig.newInstance(this)
val Context.sdCardPath: String get() = baseConfig.sdCardPath
val Context.internalStoragePath: String get() = baseConfig.internalStoragePath
val Context.otgPath: String get() = baseConfig.otgPath

fun isFingerPrintSensorAvailable() = Reprint.isHardwarePresent()

private fun Context.createCasualFileOutputStream(targetFile: File): OutputStream? {
    if (targetFile.parentFile?.exists() == false) {
        targetFile.parentFile?.mkdirs()
    }

    return try {
        FileOutputStream(targetFile)
    } catch (e: Exception) {
        showErrorToast(e)
        null
    }
}


fun Context.getFileOutputStreamSync(
    path: String,
    mimeType: String,
    parentDocumentFile: DocumentFile? = null
): OutputStream? {
    val targetFile = File(path)

    return when {
        isRestrictedSAFOnlyRoot(path) -> {
            val uri = getAndroidSAFUri(path)
            if (!getDoesFilePathExist(path)) {
                createAndroidSAFFile(path)
            }
            applicationContext.contentResolver.openOutputStream(uri, "wt")
        }

        needsStupidWritePermissions(path) -> {
            var documentFile = parentDocumentFile
            if (documentFile == null) {
                if (getDoesFilePathExist(targetFile.parentFile!!.absolutePath)) {
                    documentFile = getDocumentFile(targetFile.parent!!)
                } else {
                    documentFile = getDocumentFile(targetFile.parentFile!!.parent!!)
                    documentFile = documentFile!!.createDirectory(targetFile.parentFile!!.name)
                        ?: getDocumentFile(targetFile.parentFile!!.absolutePath)
                }
            }

            if (documentFile == null) {
                val casualOutputStream = createCasualFileOutputStream(targetFile)
                return if (casualOutputStream == null) {
                    showFileCreateError(targetFile.parent!!)
                    null
                } else {
                    casualOutputStream
                }
            }

            try {
                val uri = if (getDoesFilePathExist(path)) {
                    createDocumentUriFromRootTree(path)
                } else {
                    documentFile.createFile(mimeType, path.getFilenameFromPath())!!.uri
                }
                applicationContext.contentResolver.openOutputStream(uri, "wt")
            } catch (e: Exception) {
                showErrorToast(e)
                null
            }
        }

        isAccessibleWithSAFSdk30(path) -> {
            try {
                val uri = createDocumentUriUsingFirstParentTreeUri(path)
                if (!getDoesFilePathExist(path)) {
                    createSAFFileSdk30(path)
                }
                applicationContext.contentResolver.openOutputStream(uri, "wt")
            } catch (e: Exception) {
                null
            } ?: createCasualFileOutputStream(targetFile)
        }

        else -> return createCasualFileOutputStream(targetFile)
    }
}

fun Context.getStorageDirectories(): Array<String> {
    val paths = HashSet<String>()

    val rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE")
    val rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET")
    if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
        getExternalFilesDirs(null).filterNotNull().map { it.absolutePath }
            .mapTo(paths) { it.substring(0, it.indexOf("Android/data")) }
    } else {
        val path = Environment.getExternalStorageDirectory().absolutePath
        val folders = Pattern.compile("/").split(path)
        val lastFolder = folders[folders.size - 1]
        var isDigit = false
        try {
            Integer.valueOf(lastFolder)
            isDigit = true
        } catch (ignored: NumberFormatException) {
        }

        val rawUserId = if (isDigit) lastFolder else ""
        if (TextUtils.isEmpty(rawUserId)) {
            paths.add(rawEmulatedStorageTarget!!)
        } else {
            paths.add(rawEmulatedStorageTarget!! + File.separator + rawUserId)
        }
    }

    if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
        val rawSecondaryStorages = rawSecondaryStoragesStr!!.split(File.pathSeparator.toRegex())
            .dropLastWhile(String::isEmpty).toTypedArray()
        Collections.addAll(paths, *rawSecondaryStorages)
    }
    return paths.map { it.trimEnd('/') }.toTypedArray()
}

fun Context.showFileCreateError(path: String) {
    val error = String.format(getString(R.string.could_not_create_file), path)
    baseConfig.sdTreeUri = ""
    showErrorToast(error)
}


// http://stackoverflow.com/a/40582634/1967672
fun Context.getSDCardPath(): String {
    val directories = getStorageDirectories().filter {
        it != getInternalStoragePath() && !it.equals(
            "/storage/emulated/0",
            true
        ) && (baseConfig.otgPartition.isEmpty() || !it.endsWith(baseConfig.otgPartition))
    }

    val fullSDpattern = Pattern.compile(SD_OTG_PATTERN)
    var sdCardPath = directories.firstOrNull { fullSDpattern.matcher(it).matches() }
        ?: directories.firstOrNull { !physicalPaths.contains(it.lowercase()) } ?: ""

    // on some devices no method retrieved any SD card path, so test if its not sdcard1 by any chance. It happened on an Android 5.1
    if (sdCardPath.trimEnd('/').isEmpty()) {
        val file = File("/storage/sdcard1")
        if (file.exists()) {
            return file.absolutePath
        }

        sdCardPath = directories.firstOrNull() ?: ""
    }

    if (sdCardPath.isEmpty()) {
        val sdPattern = Pattern.compile(SD_OTG_SHORT)
        try {
            File("/storage").listFiles()?.forEach {
                if (sdPattern.matcher(it.name).matches()) {
                    sdCardPath = "/storage/${it.name}"
                }
            }
        } catch (_: Exception) {
        }
    }

    val finalPath = sdCardPath.trimEnd('/')
    baseConfig.sdCardPath = finalPath
    return finalPath
}

// some helper functions were taken from https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
fun Context.getRealPathFromURI(uri: Uri): String? {
    if (uri.scheme == "file") {
        return uri.path
    }

    if (isDownloadsDocument(uri)) {
        val id = DocumentsContract.getDocumentId(uri)
        if (id.areDigitsOnly()) {
            val newUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                id.toLong()
            )
            val path = getDataColumn(newUri)
            if (path != null) {
                return path
            }
        }
    } else if (isExternalStorageDocument(uri)) {
        val documentId = DocumentsContract.getDocumentId(uri)
        val parts = documentId.split(":")
        if (parts[0].equals("primary", true)) {
            return "${Environment.getExternalStorageDirectory().absolutePath}/${parts[1]}"
        }
    } else if (isMediaDocument(uri)) {
        val documentId = DocumentsContract.getDocumentId(uri)
        val split = documentId.split(":").dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]

        val contentUri = when (type) {
            "video" -> Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> Audio.Media.EXTERNAL_CONTENT_URI
            else -> Images.Media.EXTERNAL_CONTENT_URI
        }

        val selection = "_id=?"
        val selectionArgs = arrayOf(split[1])
        val path = getDataColumn(contentUri, selection, selectionArgs)
        if (path != null) {
            return path
        }
    }

    return getDataColumn(uri)
}

fun Context.getDataColumn(
    uri: Uri,
    selection: String? = null,
    selectionArgs: Array<String>? = null
): String? {
    try {
        val projection = arrayOf(Files.FileColumns.DATA)
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                val data = cursor.getStringValue(Files.FileColumns.DATA)
                if (data != "null") {
                    return data
                }
            }
        }
    } catch (_: Exception) {
    }
    return null
}

private fun isMediaDocument(uri: Uri) = uri.authority == "com.android.providers.media.documents"

private fun isDownloadsDocument(uri: Uri) =
    uri.authority == "com.android.providers.downloads.documents"

private fun isExternalStorageDocument(uri: Uri) =
    uri.authority == "com.android.externalstorage.documents"

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun Context.hasPermission(permId: Int) = ContextCompat.checkSelfPermission(
    this,
    getPermissionString(permId)
) == PackageManager.PERMISSION_GRANTED

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun Context.hasAllPermissions(permIds: Collection<Int>) = permIds.all(this::hasPermission)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun getPermissionString(id: Int) = when (id) {
    PERMISSION_READ_STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE
    PERMISSION_WRITE_STORAGE -> Manifest.permission.WRITE_EXTERNAL_STORAGE
    PERMISSION_CAMERA -> Manifest.permission.CAMERA
    PERMISSION_RECORD_AUDIO -> Manifest.permission.RECORD_AUDIO
    PERMISSION_READ_CONTACTS -> Manifest.permission.READ_CONTACTS
    PERMISSION_WRITE_CONTACTS -> Manifest.permission.WRITE_CONTACTS
    PERMISSION_READ_CALENDAR -> Manifest.permission.READ_CALENDAR
    PERMISSION_WRITE_CALENDAR -> Manifest.permission.WRITE_CALENDAR
    PERMISSION_CALL_PHONE -> Manifest.permission.CALL_PHONE
    PERMISSION_READ_CALL_LOG -> Manifest.permission.READ_CALL_LOG
    PERMISSION_WRITE_CALL_LOG -> Manifest.permission.WRITE_CALL_LOG
    PERMISSION_GET_ACCOUNTS -> Manifest.permission.GET_ACCOUNTS
    PERMISSION_READ_SMS -> Manifest.permission.READ_SMS
    PERMISSION_SEND_SMS -> Manifest.permission.SEND_SMS
    PERMISSION_READ_PHONE_STATE -> Manifest.permission.READ_PHONE_STATE
    PERMISSION_MEDIA_LOCATION -> if (isQPlus()) Manifest.permission.ACCESS_MEDIA_LOCATION else ""
    PERMISSION_POST_NOTIFICATIONS -> Manifest.permission.POST_NOTIFICATIONS
    PERMISSION_READ_MEDIA_IMAGES -> Manifest.permission.READ_MEDIA_IMAGES
    PERMISSION_READ_MEDIA_VIDEO -> Manifest.permission.READ_MEDIA_VIDEO
    PERMISSION_READ_MEDIA_AUDIO -> Manifest.permission.READ_MEDIA_AUDIO
    PERMISSION_ACCESS_COARSE_LOCATION -> Manifest.permission.ACCESS_COARSE_LOCATION
    PERMISSION_ACCESS_FINE_LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
    PERMISSION_READ_MEDIA_VISUAL_USER_SELECTED -> Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
    PERMISSION_READ_SYNC_SETTINGS -> Manifest.permission.READ_SYNC_SETTINGS
    else -> ""
}

fun Context.deleteAndroidSAFDirectory(
    path: String,
    allowDeleteFolder: Boolean = false,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    val treeUri = getAndroidTreeUri(path).toUri()
    val documentId = createAndroidSAFDocumentId(path)
    try {
        val uri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        val document = DocumentFile.fromSingleUri(this, uri)
        val fileDeleted =
            (document!!.isFile || allowDeleteFolder) && DocumentsContract.deleteDocument(
                applicationContext.contentResolver,
                document.uri
            )
        callback?.invoke(fileDeleted)
    } catch (e: Exception) {
        showErrorToast(e)
        callback?.invoke(false)
        storeAndroidTreeUri(path, "")
    }
}

// handle system default theme (Material You) specially as the color is taken from the system, not hardcoded by us
fun Context.getProperTextColor() = if (baseConfig.isUsingSystemTheme) {
    resources.getColor(R.color.you_neutral_text_color, theme)
} else {
    baseConfig.textColor
}

fun Context.rescanPath(path: String, callback: (() -> Unit)? = null) {
    rescanPaths(arrayListOf(path), callback)
}

fun Context.hasOTGConnected(): Boolean {
    return try {
        (getSystemService(Context.USB_SERVICE) as UsbManager).deviceList.any {
            it.value.getInterface(0).interfaceClass == UsbConstants.USB_CLASS_MASS_STORAGE
        }
    } catch (e: Exception) {
        false
    }
}

fun Context.scanPathsRecursively(paths: List<String>, callback: (() -> Unit)? = null) {
    val allPaths = java.util.ArrayList<String>()
    for (path in paths) {
        allPaths.addAll(getPaths(File(path)))
    }
    rescanPaths(allPaths, callback)
}

fun Context.hasExternalSDCard() = sdCardPath.isNotEmpty()

// no need to use DocumentFile if an SD card is set as the default storage
fun Context.needsStupidWritePermissions(path: String) =
    !isRPlus() && (isPathOnSD(path) || isPathOnOTG(path)) && !isSDCardSetAsDefaultStorage()


// avoid calling this multiple times in row, it can delete whole folder contents
fun Context.rescanPaths(paths: List<String>, callback: (() -> Unit)? = null) {
    if (paths.isEmpty()) {
        callback?.invoke()
        return
    }

    for (path in paths) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
            data = Uri.fromFile(File(path))
            sendBroadcast(this)
        }
    }

    var cnt = paths.size
    MediaScannerConnection.scanFile(applicationContext, paths.toTypedArray(), null) { _, _ ->
        if (--cnt == 0) {
            callback?.invoke()
        }
    }
}

fun Context.isSDCardSetAsDefaultStorage() =
    sdCardPath.isNotEmpty() && Environment.getExternalStorageDirectory().absolutePath.equals(
        sdCardPath,
        true
    )

fun getInternalStoragePath() =
    if (File("/storage/emulated/0").exists()) "/storage/emulated/0" else Environment.getExternalStorageDirectory().absolutePath.trimEnd(
        '/'
    )

fun Context.scanPathRecursively(path: String, callback: (() -> Unit)? = null) {
    scanPathsRecursively(arrayListOf(path), callback)
}


fun Context.createDirectorySync(directory: String): Boolean {
    if (getDoesFilePathExist(directory)) {
        return true
    }

    if (needsStupidWritePermissions(directory)) {
        val documentFile = getDocumentFile(directory.getParentPath()) ?: return false
        val newDir =
            documentFile.createDirectory(directory.getFilenameFromPath()) ?: getDocumentFile(
                directory
            )
        return newDir != null
    }

    if (isRestrictedSAFOnlyRoot(directory)) {
        return createAndroidSAFDirectory(directory)
    }

    if (isAccessibleWithSAFSdk30(directory)) {
        return createSAFDirectorySdk30(directory)
    }

    return File(directory).mkdirs()
}


fun Context.queryCursor(
    uri: Uri,
    projection: Array<String>,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null,
    showErrors: Boolean = false,
    callback: (cursor: Cursor) -> Unit
) {
    try {
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    callback(cursor)
                } while (cursor.moveToNext())
            }
        }
    } catch (e: Exception) {
        if (showErrors) {
            showErrorToast(e)
        }
    }
}

fun Context.isPathOnInternalStorage(path: String) =
    internalStoragePath.isNotEmpty() && path.startsWith(internalStoragePath)

fun Context.getFilenameFromContentUri(uri: Uri): String? {
    val projection = arrayOf(
        OpenableColumns.DISPLAY_NAME
    )

    try {
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                return cursor.getStringValue(OpenableColumns.DISPLAY_NAME)
            }
        }
    } catch (_: Exception) {
    }
    return null
}

fun Context.createAndroidSAFDirectory(path: String): Boolean {
    return try {
        val treeUri = getAndroidTreeUri(path).toUri()
        val parentPath = path.getParentPath()
        if (!getDoesFilePathExist(parentPath)) {
            createAndroidSAFDirectory(parentPath)
        }
        val documentId = createAndroidSAFDocumentId(parentPath)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.createDocument(
            contentResolver,
            parentUri,
            Document.MIME_TYPE_DIR,
            path.getFilenameFromPath()
        ) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.getSizeFromContentUri(uri: Uri): Long {
    val projection = arrayOf(OpenableColumns.SIZE)
    try {
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                return cursor.getLongValue(OpenableColumns.SIZE)
            }
        }
    } catch (_: Exception) {
    }
    return 0L
}

fun Context.getMyContentProviderCursorLoader() =
    CursorLoader(this, MyContentProvider.MY_CONTENT_URI, null, null, null, null)

fun getCurrentFormattedDateTime(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    return simpleDateFormat.format(Date(System.currentTimeMillis()))
}

fun Context.updateSDCardPath() {
    ensureBackgroundThread {
        val oldPath = baseConfig.sdCardPath
        baseConfig.sdCardPath = getSDCardPath()
        if (oldPath != baseConfig.sdCardPath) {
            baseConfig.sdTreeUri = ""
        }
    }
}

fun Context.isThankYouInstalled() = isPackageInstalled("com.simplemobiletools.thankyou")

fun isOrWasThankYouInstalled() = true

fun Context.isAProApp() =
    packageName.startsWith("com.simplemobiletools.") && packageName.removeSuffix(".debug")
        .endsWith(".pro")

fun Context.isPackageInstalled(pkgName: String): Boolean {
    return try {
        packageManager.getPackageInfo(pkgName, 0)
        true
    } catch (e: Exception) {
        false
    }
}

fun Context.getLaunchIntent() = packageManager.getLaunchIntentForPackage(baseConfig.appId)

fun Context.isSAFOnlyRoot(path: String): Boolean {
    return getSAFOnlyDirs().any { "${path.trimEnd('/')}/".startsWith(it) }
}

fun Context.getCanAppBeUpgraded() = proPackages.contains(
    baseConfig.appId.removeSuffix(".debug").removePrefix("com.simplemobiletools.")
)

fun Context.getStoreUrl() =
    "https://play.google.com/store/apps/details?id=${packageName.removeSuffix(".debug")}"

fun Context.getTimeFormat() = if (baseConfig.use24HourFormat) TIME_FORMAT_24 else TIME_FORMAT_12

fun Context.getAppIconColors() =
    resources.getIntArray(R.array.md_app_icon_colors).toCollection(ArrayList())

fun Context.getStringsPackageName() = getString(R.string.package_name)

fun Context.getTextSize() = when (baseConfig.fontSize) {
    FONT_SIZE_SMALL -> resources.getDimension(R.dimen.smaller_text_size)
    FONT_SIZE_MEDIUM -> resources.getDimension(R.dimen.bigger_text_size)
    FONT_SIZE_LARGE -> resources.getDimension(R.dimen.big_text_size)
    else -> resources.getDimension(R.dimen.extra_big_text_size)
}

val Context.windowManager: WindowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager
val Context.notificationManager: NotificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
val Context.shortcutManager: ShortcutManager
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    get() = getSystemService(ShortcutManager::class.java) as ShortcutManager


val Context.navigationBarOnSide: Boolean get() = usableScreenSize.x < realScreenSize.x && usableScreenSize.x > usableScreenSize.y
val Context.navigationBarOnBottom: Boolean get() = usableScreenSize.y < realScreenSize.y
val Context.navigationBarHeight: Int get() = if (navigationBarOnBottom && navigationBarSize.y != usableScreenSize.y) navigationBarSize.y else 0

val Context.navigationBarSize: Point
    get() = when {
        navigationBarOnSide -> Point(newNavigationBarHeight, usableScreenSize.y)
        navigationBarOnBottom -> Point(usableScreenSize.x, newNavigationBarHeight)
        else -> Point()
    }

val Context.newNavigationBarHeight: Int
    @SuppressLint("InternalInsetResource")
    get() {
        var navigationBarHeight = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return navigationBarHeight
    }

val Context.statusBarHeight: Int
    @SuppressLint("InternalInsetResource")
    get() {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

val Context.usableScreenSize: Point
    get() {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        return size
    }

val Context.realScreenSize: Point
    get() {
        val size = Point()
        windowManager.defaultDisplay.getRealSize(size)
        return size
    }

fun Context.isUsingGestureNavigation(): Boolean {
    return try {
        val resourceId =
            resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
        if (resourceId > 0) {
            resources.getInteger(resourceId) == 2
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
}

fun Context.copyToClipboard(text: String) {
    val clip = ClipData.newPlainText(getString(R.string.simple_commons), text)
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
    val toastText = String.format(getString(R.string.value_copied_to_clipboard_show), text)
    toast(toastText)
}

fun Context.updateTextColors(viewGroup: ViewGroup) {
    val textColor = when {
        baseConfig.isUsingSystemTheme -> getProperTextColor()
        else -> baseConfig.textColor
    }

    val backgroundColor = baseConfig.backgroundColor
    val accentColor = when {
        isWhiteTheme() || isBlackAndWhiteTheme() -> baseConfig.accentColor
        else -> getProperPrimaryColor()
    }

    val cnt = viewGroup.childCount
    (0 until cnt).map { viewGroup.getChildAt(it) }.forEach {
        when (it) {
            is MyTextView -> it.setColors(textColor, accentColor)
            is MyAppCompatSpinner -> it.setColors(textColor, backgroundColor)
            is MyCompatRadioButton -> it.setColors(textColor, accentColor)
            is MyAppCompatCheckbox -> it.setColors(textColor, accentColor)
            is MyEditText -> it.setColors(textColor, accentColor)
            is MyAutoCompleteTextView -> it.setColors(textColor, accentColor)
            is MyFloatingActionButton -> it.setColors(accentColor)
            is MySeekBar -> it.setColors(accentColor)
            is MyButton -> it.setColors(textColor)
            is MyTextInputLayout -> it.setColors(textColor, accentColor)
            is ViewGroup -> updateTextColors(it)
        }
    }
}

fun Context.checkAppIconColor() {
    val appId = baseConfig.appId
    if (appId.isNotEmpty() && baseConfig.lastIconColor != baseConfig.appIconColor) {
        getAppIconColors().forEachIndexed { index, color ->
            toggleAppIconColor(appId, index, color, false)
        }

        getAppIconColors().forEachIndexed { index, color ->
            if (baseConfig.appIconColor == color) {
                toggleAppIconColor(appId, index, color, true)
            }
        }
    }
}

fun Context.openNotificationSettings() {
    if (isOreoPlus()) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
    } else {
        // For Android versions below Oreo, you can't directly open the app's notification settings.
        // You can open the general notification settings instead.
        val intent = Intent(Settings.ACTION_SETTINGS)
        startActivity(intent)
    }
}

fun Context.openDeviceSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }

    try {
        startActivity(intent)
    } catch (e: Exception) {
        showErrorToast(e)
    }
}

fun Context.getAndroidSAFUri(path: String): Uri {
    val treeUri = getAndroidTreeUri(path).toUri()
    val documentId = createAndroidSAFDocumentId(path)
    return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
}

fun Context.getAndroidSAFDocument(path: String): DocumentFile? {
    val basePath = path.getBasePath(this)
    val androidPath = File(basePath, "Android").path
    var relativePath = path.substring(androidPath.length)
    if (relativePath.startsWith(File.separator)) {
        relativePath = relativePath.substring(1)
    }

    return try {
        val treeUri = getAndroidTreeUri(path).toUri()
        var document = DocumentFile.fromTreeUri(applicationContext, treeUri)
        val parts = relativePath.split("/").filter { it.isNotEmpty() }
        for (part in parts) {
            document = document?.findFile(part)
        }
        document
    } catch (ignored: Exception) {
        null
    }
}

fun Context.getSomeAndroidSAFDocument(path: String): DocumentFile? =
    getFastAndroidSAFDocument(path) ?: getAndroidSAFDocument(path)

fun Context.getFastAndroidSAFDocument(path: String): DocumentFile? {
    val treeUri = getAndroidTreeUri(path)
    if (treeUri.isEmpty()) {
        return null
    }

    val uri = getAndroidSAFUri(path)
    return DocumentFile.fromSingleUri(this, uri)
}

fun Context.renameAndroidSAFDocument(oldPath: String, newPath: String): Boolean {
    return try {
        val treeUri = getAndroidTreeUri(oldPath).toUri()
        val documentId = createAndroidSAFDocumentId(oldPath)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.renameDocument(
            contentResolver,
            parentUri,
            newPath.getFilenameFromPath()
        ) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.getAndroidSAFFileSize(path: String): Long {
    val treeUri = getAndroidTreeUri(path).toUri()
    val documentId = createAndroidSAFDocumentId(path)
    return getFileSize(treeUri, documentId)
}

fun Context.getAndroidTreeUri(path: String): String {
    return when {
        isPathOnOTG(path) -> if (isAndroidDataDir(path)) baseConfig.otgAndroidDataTreeUri else baseConfig.otgAndroidObbTreeUri
        isPathOnSD(path) -> if (isAndroidDataDir(path)) baseConfig.sdAndroidDataTreeUri else baseConfig.sdAndroidObbTreeUri
        else -> if (isAndroidDataDir(path)) baseConfig.primaryAndroidDataTreeUri else baseConfig.primaryAndroidObbTreeUri
    }
}

fun isAndroidDataDir(path: String): Boolean {
    val resolvedPath = "${path.trimEnd('/')}/"
    return resolvedPath.contains(ANDROID_DATA_DIR)
}

fun Context.storeAndroidTreeUri(path: String, treeUri: String) {
    return when {
        isPathOnOTG(path) -> if (isAndroidDataDir(path)) baseConfig.otgAndroidDataTreeUri =
            treeUri else baseConfig.otgAndroidObbTreeUri = treeUri

        isPathOnSD(path) -> if (isAndroidDataDir(path)) baseConfig.sdAndroidDataTreeUri =
            treeUri else baseConfig.sdAndroidObbTreeUri = treeUri

        else -> if (isAndroidDataDir(path)) baseConfig.primaryAndroidDataTreeUri =
            treeUri else baseConfig.primaryAndroidObbTreeUri = treeUri
    }
}

fun Context.getSAFStorageId(fullPath: String): String {
    return if (fullPath.startsWith('/')) {
        when {
            fullPath.startsWith(internalStoragePath) -> "primary"
            else -> fullPath.substringAfter("/storage/", "").substringBefore('/')
        }
    } else {
        fullPath.substringBefore(':', "").substringAfterLast('/')
    }
}

fun Context.getStorageRootIdForAndroidDir(path: String) =
    getAndroidTreeUri(path).removeSuffix(if (isAndroidDataDir(path)) "%3AAndroid%2Fdata" else "%3AAndroid%2Fobb")
        .substringAfterLast('/').trimEnd('/')

fun Context.isAStorageRootFolder(path: String): Boolean {
    val trimmed = path.trimEnd('/')
    return trimmed.isEmpty() || trimmed.equals(internalStoragePath, true) || trimmed.equals(
        sdCardPath,
        true
    ) || trimmed.equals(otgPath, true)
}

fun Context.tryFastDocumentDelete(path: String, allowDeleteFolder: Boolean): Boolean {
    val document = getFastDocumentFile(path)
    return if (document?.isFile == true || allowDeleteFolder) {
        try {
            DocumentsContract.deleteDocument(contentResolver, document?.uri!!)
        } catch (e: Exception) {
            false
        }
    } else {
        false
    }
}

fun Context.getFastDocumentFile(path: String): DocumentFile? {
    if (isPathOnOTG(path)) {
        return getOTGFastDocumentFile(path)
    }

    if (baseConfig.sdCardPath.isEmpty()) {
        return null
    }

    val relativePath = Uri.encode(path.substring(baseConfig.sdCardPath.length).trim('/'))
    val externalPathPart =
        baseConfig.sdCardPath.split("/").lastOrNull(String::isNotEmpty)?.trim('/') ?: return null
    val fullUri = "${baseConfig.sdTreeUri}/document/$externalPathPart%3A$relativePath"
    return DocumentFile.fromSingleUri(this, Uri.parse(fullUri))
}

fun Context.getOTGFastDocumentFile(path: String, otgPathToUse: String? = null): DocumentFile? {
    if (baseConfig.otgTreeUri.isEmpty()) {
        return null
    }

    val otgPath = otgPathToUse ?: baseConfig.otgPath
    if (baseConfig.otgPartition.isEmpty()) {
        baseConfig.otgPartition =
            baseConfig.otgTreeUri.removeSuffix("%3A").substringAfterLast('/').trimEnd('/')
        updateOTGPathFromPartition()
    }

    val relativePath = Uri.encode(path.substring(otgPath.length).trim('/'))
    val fullUri = "${baseConfig.otgTreeUri}/document/${baseConfig.otgPartition}%3A$relativePath"
    return DocumentFile.fromSingleUri(this, Uri.parse(fullUri))
}

fun Context.getSomeDocumentFile(path: String) = getFastDocumentFile(path) ?: getDocumentFile(path)

fun getPaths(file: File): java.util.ArrayList<String> {
    val paths = arrayListOf<String>(file.absolutePath)
    if (file.isDirectory) {
        val files = file.listFiles() ?: return paths
        for (curFile in files) {
            paths.addAll(getPaths(curFile))
        }
    }
    return paths
}

fun getFileUri(path: String): Uri = when {
    path.isImageSlow() -> Images.Media.EXTERNAL_CONTENT_URI
    path.isVideoSlow() -> Video.Media.EXTERNAL_CONTENT_URI
    path.isAudioSlow() -> Audio.Media.EXTERNAL_CONTENT_URI
    else -> Files.getContentUri("external")
}

fun Context.rescanAndDeletePath(path: String, callback: () -> Unit) {
    val scanFileMaxDuration = 1000L
    val scanFileHandler = Handler(Looper.getMainLooper())
    scanFileHandler.postDelayed({
        callback()
    }, scanFileMaxDuration)

    MediaScannerConnection.scanFile(applicationContext, arrayOf(path), null) { _, uri ->
        scanFileHandler.removeCallbacksAndMessages(null)
        try {
            applicationContext.contentResolver.delete(uri, null, null)
        } catch (_: Exception) {
        }
        callback()
    }
}

fun Context.updateInMediaStore(oldPath: String, newPath: String) {
    ensureBackgroundThread {
        val values = ContentValues().apply {
            put(MediaColumns.DATA, newPath)
            put(MediaColumns.DISPLAY_NAME, newPath.getFilenameFromPath())
            put(MediaColumns.TITLE, newPath.getFilenameFromPath())
        }
        val uri = getFileUri(oldPath)
        val selection = "${MediaColumns.DATA} = ?"
        val selectionArgs = arrayOf(oldPath)

        try {
            contentResolver.update(uri, values, selection, selectionArgs)
        } catch (ignored: Exception) {
        }
    }
}

fun Context.updateLastModified(path: String, lastModified: Long) {
    val values = ContentValues().apply {
        put(MediaColumns.DATE_MODIFIED, lastModified / 1000)
    }
    File(path).setLastModified(lastModified)
    val uri = getFileUri(path)
    val selection = "${MediaColumns.DATA} = ?"
    val selectionArgs = arrayOf(path)

    try {
        contentResolver.update(uri, values, selection, selectionArgs)
    } catch (ignored: Exception) {
    }
}

fun Context.getOTGItems(
    path: String,
    shouldShowHidden: Boolean,
    getProperFileSize: Boolean,
    callback: (java.util.ArrayList<FileDirItem>) -> Unit
) {
    val items = java.util.ArrayList<FileDirItem>()
    val otgTreeUri = baseConfig.otgTreeUri
    var rootUri = try {
        DocumentFile.fromTreeUri(applicationContext, Uri.parse(otgTreeUri))
    } catch (e: Exception) {
        showErrorToast(e)
        baseConfig.otgPath = ""
        baseConfig.otgTreeUri = ""
        baseConfig.otgPartition = ""
        null
    }

    if (rootUri == null) {
        callback(items)
        return
    }

    val parts = path.split("/").dropLastWhile { it.isEmpty() }
    for (part in parts) {
        if (path == otgPath) {
            break
        }

        if (part == "otg:" || part == "") {
            continue
        }

        val file = rootUri!!.findFile(part)
        if (file != null) {
            rootUri = file
        }
    }

    val files = rootUri!!.listFiles().filter { it.exists() }

    val basePath = "${baseConfig.otgTreeUri}/document/${baseConfig.otgPartition}%3A"
    for (file in files) {
        val name = file.name ?: continue
        if (!shouldShowHidden && name.startsWith(".")) {
            continue
        }

        val isDirectory = file.isDirectory
        val filePath = file.uri.toString().substring(basePath.length)
        val decodedPath = otgPath + "/" + URLDecoder.decode(filePath, "UTF-8")
        val fileSize = when {
            getProperFileSize -> file.getItemSize(shouldShowHidden)
            isDirectory -> 0L
            else -> file.length()
        }

        val childrenCount = if (isDirectory) {
            file.listFiles().size
        } else {
            0
        }

        val lastModified = file.lastModified()
        val fileDirItem =
            FileDirItem(decodedPath, name, isDirectory, childrenCount, fileSize, lastModified)
        items.add(fileDirItem)
    }

    callback(items)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.getAndroidSAFFileItems(
    path: String,
    shouldShowHidden: Boolean,
    getProperFileSize: Boolean = true,
    callback: (java.util.ArrayList<FileDirItem>) -> Unit
) {
    val items = java.util.ArrayList<FileDirItem>()
    val rootDocId = getStorageRootIdForAndroidDir(path)
    val treeUri = getAndroidTreeUri(path).toUri()
    val documentId = createAndroidSAFDocumentId(path)
    val childrenUri = try {
        DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId)
    } catch (e: Exception) {
        showErrorToast(e)
        storeAndroidTreeUri(path, "")
        null
    }

    if (childrenUri == null) {
        callback(items)
        return
    }

    val projection = arrayOf(
        Document.COLUMN_DOCUMENT_ID,
        Document.COLUMN_DISPLAY_NAME,
        Document.COLUMN_MIME_TYPE,
        Document.COLUMN_LAST_MODIFIED
    )
    try {
        val rawCursor = contentResolver.query(childrenUri, projection, null, null)!!
        val cursor =
            ExternalStorageProviderHack.transformQueryResult(rootDocId, childrenUri, rawCursor)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val docId = cursor.getStringValue(Document.COLUMN_DOCUMENT_ID)
                    val name = cursor.getStringValue(Document.COLUMN_DISPLAY_NAME)
                    val mimeType = cursor.getStringValue(Document.COLUMN_MIME_TYPE)
                    val lastModified = cursor.getLongValue(Document.COLUMN_LAST_MODIFIED)
                    val isDirectory = mimeType == Document.MIME_TYPE_DIR
                    val filePath = docId.substring("${getStorageRootIdForAndroidDir(path)}:".length)
                    if (!shouldShowHidden && name.startsWith(".")) {
                        continue
                    }

                    val decodedPath =
                        path.getBasePath(this) + "/" + URLDecoder.decode(filePath, "UTF-8")
                    val fileSize = when {
                        getProperFileSize -> getFileSize(treeUri, docId)
                        isDirectory -> 0L
                        else -> getFileSize(treeUri, docId)
                    }

                    val childrenCount = if (isDirectory) {
                        getDirectChildrenCount(rootDocId, treeUri, docId, shouldShowHidden)
                    } else {
                        0
                    }

                    val fileDirItem = FileDirItem(
                        decodedPath,
                        name,
                        isDirectory,
                        childrenCount,
                        fileSize,
                        lastModified
                    )
                    items.add(fileDirItem)
                } while (cursor.moveToNext())
            }
        }
    } catch (e: Exception) {
        showErrorToast(e)
    }
    callback(items)
}

fun Context.getDirectChildrenCount(
    rootDocId: String,
    treeUri: Uri,
    documentId: String,
    shouldShowHidden: Boolean
): Int {
    return try {
        val projection = arrayOf(Document.COLUMN_DOCUMENT_ID)
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId)
        val rawCursor = contentResolver.query(childrenUri, projection, null, null, null)!!
        val cursor =
            ExternalStorageProviderHack.transformQueryResult(rootDocId, childrenUri, rawCursor)
        if (shouldShowHidden) {
            cursor.count
        } else {
            var count = 0
            cursor.use {
                while (cursor.moveToNext()) {
                    val docId = cursor.getStringValue(Document.COLUMN_DOCUMENT_ID)
                    if (!docId.getFilenameFromPath().startsWith('.') || shouldShowHidden) {
                        count++
                    }
                }
            }
            count
        }
    } catch (e: Exception) {
        0
    }
}

fun Context.getFileSize(treeUri: Uri, documentId: String): Long {
    val projection = arrayOf(Document.COLUMN_SIZE)
    val documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
    return contentResolver.query(documentUri, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            cursor.getLongValue(Document.COLUMN_SIZE)
        } else {
            0L
        }
    } ?: 0L
}

fun Context.getAndroidSAFDirectChildrenCount(path: String, countHidden: Boolean): Int {
    val treeUri = getAndroidTreeUri(path).toUri()
    if (treeUri == Uri.EMPTY) {
        return 0
    }

    val documentId = createAndroidSAFDocumentId(path)
    val rootDocId = getStorageRootIdForAndroidDir(path)
    return getDirectChildrenCount(rootDocId, treeUri, documentId, countHidden)
}

fun Context.trySAFFileDelete(
    fileDirItem: FileDirItem,
    allowDeleteFolder: Boolean = false,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    var fileDeleted = tryFastDocumentDelete(fileDirItem.path, allowDeleteFolder)
    if (!fileDeleted) {
        val document = getDocumentFile(fileDirItem.path)
        if (document != null && (fileDirItem.isDirectory == document.isDirectory)) {
            try {
                fileDeleted =
                    (document.isFile || allowDeleteFolder) && DocumentsContract.deleteDocument(
                        applicationContext.contentResolver,
                        document.uri
                    )
            } catch (ignored: Exception) {
                baseConfig.sdTreeUri = ""
                baseConfig.sdCardPath = ""
            }
        }
    }

    if (fileDeleted) {
        deleteFromMediaStore(fileDirItem.path)
        callback?.invoke(true)
    }
}

fun Context.getFileInputStreamSync(path: String): InputStream? {
    return when {
        isRestrictedSAFOnlyRoot(path) -> {
            val uri = getAndroidSAFUri(path)
            applicationContext.contentResolver.openInputStream(uri)
        }

        isAccessibleWithSAFSdk30(path) -> {
            try {
                FileInputStream(File(path))
            } catch (e: Exception) {
                val uri = createDocumentUriUsingFirstParentTreeUri(path)
                applicationContext.contentResolver.openInputStream(uri)
            }
        }

        isPathOnOTG(path) -> {
            val fileDocument = getSomeDocumentFile(path)
            applicationContext.contentResolver.openInputStream(fileDocument?.uri!!)
        }

        else -> FileInputStream(File(path))
    }
}

fun Context.updateOTGPathFromPartition() {
    val otgPath = "/storage/${baseConfig.otgPartition}"
    baseConfig.otgPath = if (getOTGFastDocumentFile(otgPath, otgPath)?.exists() == true) {
        "/storage/${baseConfig.otgPartition}"
    } else {
        "/mnt/media_rw/${baseConfig.otgPartition}"
    }
}

fun Context.getDoesFilePathExist(path: String, otgPathToUse: String? = null): Boolean {
    val otgPath = otgPathToUse ?: baseConfig.otgPath
    return when {
        isRestrictedSAFOnlyRoot(path) -> getFastAndroidSAFDocument(path)?.exists() ?: false
        otgPath.isNotEmpty() && path.startsWith(otgPath) -> getOTGFastDocumentFile(path)?.exists()
            ?: false

        else -> File(path).exists()
    }
}

fun Context.getIsPathDirectory(path: String): Boolean {
    return when {
        isRestrictedSAFOnlyRoot(path) -> getFastAndroidSAFDocument(path)?.isDirectory ?: false
        isPathOnOTG(path) -> getOTGFastDocumentFile(path)?.isDirectory ?: false
        else -> File(path).isDirectory
    }
}

fun Context.getFolderLastModifieds(folder: String): java.util.HashMap<String, Long> {
    val lastModifieds = java.util.HashMap<String, Long>()
    val projection = arrayOf(
        Images.Media.DISPLAY_NAME,
        Images.Media.DATE_MODIFIED
    )

    val uri = Files.getContentUri("external")
    val selection =
        "${Images.Media.DATA} LIKE ? AND ${Images.Media.DATA} NOT LIKE ? AND ${Images.Media.MIME_TYPE} IS NOT NULL" // avoid selecting folders
    val selectionArgs = arrayOf("$folder/%", "$folder/%/%")

    try {
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        val lastModified = cursor.getLongValue(Images.Media.DATE_MODIFIED) * 1000
                        if (lastModified != 0L) {
                            val name = cursor.getStringValue(Images.Media.DISPLAY_NAME)
                            lastModifieds["$folder/$name"] = lastModified
                        }
                    } catch (_: Exception) {
                    }
                } while (cursor.moveToNext())
            }
        }
    } catch (_: Exception) {
    }

    return lastModifieds
}

// avoid these being set as SD card paths
private val physicalPaths = arrayListOf(
    "/storage/sdcard1", // Motorola Xoom
    "/storage/extsdcard", // Samsung SGS3
    "/storage/sdcard0/external_sdcard", // User request
    "/mnt/extsdcard", "/mnt/sdcard/external_sd", // Samsung galaxy family
    "/mnt/external_sd", "/mnt/media_rw/sdcard1", // 4.4.2 on CyanogenMod S3
    "/removable/microsd", // Asus transformer prime
    "/mnt/emmc", "/storage/external_SD", // LG
    "/storage/ext_sd", // HTC One Max
    "/storage/removable/sdcard1", // Sony Xperia Z1
    "/data/sdext", "/data/sdext2", "/data/sdext3", "/data/sdext4", "/sdcard1", // Sony Xperia Z
    "/sdcard2", // HTC One M8s
    "/storage/usbdisk0",
    "/storage/usbdisk1",
    "/storage/usbdisk2"
)

// Convert paths like /storage/emulated/0/Pictures/Screenshots/first.jpg to content://media/external/images/media/131799
// so that we can refer to the file in the MediaStore.
// If we found no mediastore uri for a given file, do not return its path either to avoid some mismatching
fun Context.getUrisPathsFromFileDirItems(fileDirItems: List<FileDirItem>): Pair<java.util.ArrayList<String>, java.util.ArrayList<Uri>> {
    val fileUris = java.util.ArrayList<Uri>()
    val successfulFilePaths = java.util.ArrayList<String>()
    val allIds = getMediaStoreIds(this)
    val filePaths = fileDirItems.map { it.path }
    filePaths.forEach { path ->
        for ((filePath, mediaStoreId) in allIds) {
            if (filePath.lowercase() == path.lowercase()) {
                val baseUri = getFileUri(filePath)
                val uri = ContentUris.withAppendedId(baseUri, mediaStoreId)
                fileUris.add(uri)
                successfulFilePaths.add(path)
            }
        }
    }

    return Pair(successfulFilePaths, fileUris)
}

fun getMediaStoreIds(context: Context): java.util.HashMap<String, Long> {
    val ids = java.util.HashMap<String, Long>()
    val projection = arrayOf(
        Images.Media.DATA,
        Images.Media._ID
    )

    val uri = Files.getContentUri("external")

    try {
        context.queryCursor(uri, projection) { cursor ->
            try {
                val id = cursor.getLongValue(Images.Media._ID)
                if (id != 0L) {
                    val path = cursor.getStringValue(Images.Media.DATA)
                    ids[path] = id
                }
            } catch (_: Exception) {
            }
        }
    } catch (_: Exception) {
    }

    return ids
}

fun Context.getFileUrisFromFileDirItems(fileDirItems: List<FileDirItem>): List<Uri> {
    val fileUris = getUrisPathsFromFileDirItems(fileDirItems).second
    if (fileUris.isEmpty()) {
        fileDirItems.map { fileDirItem ->
            fileUris.add(fileDirItem.assembleContentUri())
        }
    }

    return fileUris
}

fun Context.getProperBackgroundColor() = if (baseConfig.isUsingSystemTheme) {
    resources.getColor(R.color.you_background_color, theme)
} else {
    baseConfig.backgroundColor
}

fun Context.getProperPrimaryColor() = when {
    baseConfig.isUsingSystemTheme -> resources.getColor(R.color.you_primary_color, theme)
    isWhiteTheme() || isBlackAndWhiteTheme() -> baseConfig.accentColor
    else -> baseConfig.primaryColor
}

fun Context.getProperStatusBarColor() = when {
    baseConfig.isUsingSystemTheme -> resources.getColor(R.color.you_status_bar_color, theme)
    else -> getProperBackgroundColor()
}

// get the color of the StatusBar with material activity, if the layout is scrolled down a bit
fun Context.getColoredMaterialStatusBarColor(): Int {
    return if (baseConfig.isUsingSystemTheme) {
        resources.getColor(R.color.you_status_bar_color, theme)
    } else {
        getProperPrimaryColor()
    }
}

fun Context.isBlackAndWhiteTheme() =
    baseConfig.textColor == Color.WHITE && baseConfig.primaryColor == Color.BLACK && baseConfig.backgroundColor == Color.BLACK

fun Context.isWhiteTheme() =
    baseConfig.textColor == DARK_GREY && baseConfig.primaryColor == Color.WHITE && baseConfig.backgroundColor == Color.WHITE

fun Context.isUsingSystemDarkTheme() =
    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_YES != 0

fun Context.getSharedTheme(callback: (sharedTheme: SharedTheme?) -> Unit) {
    if (!isThankYouInstalled()) {
        callback(null)
    } else {
        val cursorLoader = getMyContentProviderCursorLoader()
        ensureBackgroundThread {
            callback(getSharedThemeSync(cursorLoader))
        }
    }
}

fun getSharedThemeSync(cursorLoader: CursorLoader): SharedTheme? {
    val cursor = cursorLoader.loadInBackground()
    cursor?.use {
        if (cursor.moveToFirst()) {
            try {
                val textColor = cursor.getIntValue(MyContentProvider.COL_TEXT_COLOR)
                val backgroundColor = cursor.getIntValue(MyContentProvider.COL_BACKGROUND_COLOR)
                val primaryColor = cursor.getIntValue(MyContentProvider.COL_PRIMARY_COLOR)
                val accentColor = cursor.getIntValue(MyContentProvider.COL_ACCENT_COLOR)
                val appIconColor = cursor.getIntValue(MyContentProvider.COL_APP_ICON_COLOR)
                val lastUpdatedTS = cursor.getIntValue(MyContentProvider.COL_LAST_UPDATED_TS)
                return SharedTheme(
                    textColor,
                    backgroundColor,
                    primaryColor,
                    appIconColor,
                    lastUpdatedTS,
                    accentColor
                )
            } catch (_: Exception) {
            }
        }
    }
    return null
}

fun Context.toggleAppIconColor(appId: String, colorIndex: Int, color: Int, enable: Boolean) {
    val className =
        "${appId.removeSuffix(".debug")}.activities.SplashActivity${appIconColorStrings[colorIndex]}"
    val state =
        if (enable) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    try {
        packageManager.setComponentEnabledSetting(
            ComponentName(appId, className),
            state,
            PackageManager.DONT_KILL_APP
        )
        if (enable) {
            baseConfig.lastIconColor = color
        }
    } catch (_: Exception) {
    }
}
