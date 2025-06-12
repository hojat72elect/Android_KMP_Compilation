package com.simplemobiletools.smsmessenger.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_STREAM
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.TransactionTooLargeException
import android.provider.ContactsContract
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.biometric.auth.AuthPromptCallback
import androidx.biometric.auth.AuthPromptHost
import androidx.biometric.auth.Class2BiometricAuthPrompt
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.simplemobiletools.smsmessenger.extensions.toast
import com.simplemobiletools.smsmessenger.helpers.PROTECTION_FINGERPRINT
import com.simplemobiletools.smsmessenger.models.RadioItem
import com.simplemobiletools.smsmessenger.models.Release
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.BaseSimpleActivity
import com.simplemobiletools.smsmessenger.compose.extensions.DEVELOPER_PLAY_STORE_URL
import com.simplemobiletools.smsmessenger.databinding.DialogTitleBinding
import com.simplemobiletools.smsmessenger.dialogs.AppSideloadedDialog
import com.simplemobiletools.smsmessenger.dialogs.ConfirmationAdvancedDialog
import com.simplemobiletools.smsmessenger.dialogs.CustomIntervalPickerDialog
import com.simplemobiletools.smsmessenger.dialogs.DonateDialog
import com.simplemobiletools.smsmessenger.dialogs.RadioGroupDialog
import com.simplemobiletools.smsmessenger.dialogs.RateStarsDialog
import com.simplemobiletools.smsmessenger.dialogs.SecurityDialog
import com.simplemobiletools.smsmessenger.dialogs.UpgradeToProDialog
import com.simplemobiletools.smsmessenger.dialogs.WhatsNewDialog
import com.simplemobiletools.smsmessenger.helpers.CONTACT_ID
import com.simplemobiletools.smsmessenger.helpers.CREATE_DOCUMENT_SDK_30
import com.simplemobiletools.smsmessenger.helpers.DARK_GREY
import com.simplemobiletools.smsmessenger.helpers.EXTRA_SHOW_ADVANCED
import com.simplemobiletools.smsmessenger.helpers.IS_FROM_GALLERY
import com.simplemobiletools.smsmessenger.helpers.IS_PRIVATE
import com.simplemobiletools.smsmessenger.helpers.MINUTE_SECONDS
import com.simplemobiletools.smsmessenger.helpers.MyContentProvider
import com.simplemobiletools.smsmessenger.helpers.OPEN_DOCUMENT_TREE_FOR_ANDROID_DATA_OR_OBB
import com.simplemobiletools.smsmessenger.helpers.OPEN_DOCUMENT_TREE_FOR_SDK_30
import com.simplemobiletools.smsmessenger.helpers.OPEN_DOCUMENT_TREE_OTG
import com.simplemobiletools.smsmessenger.helpers.OPEN_DOCUMENT_TREE_SD
import com.simplemobiletools.smsmessenger.helpers.PERMISSION_CALL_PHONE
import com.simplemobiletools.smsmessenger.helpers.PERMISSION_READ_STORAGE
import com.simplemobiletools.smsmessenger.helpers.REAL_FILE_PATH
import com.simplemobiletools.smsmessenger.helpers.REQUEST_EDIT_IMAGE
import com.simplemobiletools.smsmessenger.helpers.REQUEST_SET_AS
import com.simplemobiletools.smsmessenger.helpers.SIDELOADING_FALSE
import com.simplemobiletools.smsmessenger.helpers.SIDELOADING_TRUE
import com.simplemobiletools.smsmessenger.helpers.SILENT
import com.simplemobiletools.smsmessenger.helpers.SimpleContactsHelper
import com.simplemobiletools.smsmessenger.helpers.ensureBackgroundThread
import com.simplemobiletools.smsmessenger.helpers.isOnMainThread
import com.simplemobiletools.smsmessenger.helpers.isRPlus
import com.simplemobiletools.smsmessenger.models.AlarmSound
import com.simplemobiletools.smsmessenger.models.FileDirItem
import com.simplemobiletools.smsmessenger.models.SharedTheme
import com.simplemobiletools.smsmessenger.models.SimpleContact
import com.simplemobiletools.smsmessenger.views.MyTextView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale
import java.util.TreeSet

fun Activity.dialNumber(phoneNumber: String, callback: (() -> Unit)? = null) {
    hideKeyboard()
    Intent(Intent.ACTION_DIAL).apply {
        data = Uri.fromParts("tel", phoneNumber, null)

        try {
            startActivity(this)
            callback?.invoke()
        } catch (e: ActivityNotFoundException) {
            toast(R.string.no_app_found)
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }
}

fun Activity.launchViewIntent(uri: Uri, mimetype: String, filename: String) {
    Intent().apply {
        action = Intent.ACTION_VIEW
        setDataAndType(uri, mimetype.lowercase(Locale.getDefault()))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            hideKeyboard()
            startActivity(this)
        } catch (e: ActivityNotFoundException) {
            val newMimetype = filename.getMimeType()
            if (newMimetype.isNotEmpty() && mimetype != newMimetype) {
                launchViewIntent(uri, newMimetype, filename)
            } else {
                toast(R.string.no_app_found)
            }
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }
}

fun Activity.startContactDetailsIntent(contact: SimpleContact) {
    val simpleContacts = "com.simplemobiletools.contacts.pro"
    val simpleContactsDebug = "com.simplemobiletools.contacts.pro.debug"
    if (contact.rawId > 1000000 && contact.contactId > 1000000 && contact.rawId == contact.contactId &&
        (isPackageInstalled(simpleContacts) || isPackageInstalled(simpleContactsDebug))
    ) {
        Intent().apply {
            action = Intent.ACTION_VIEW
            putExtra(CONTACT_ID, contact.rawId)
            putExtra(IS_PRIVATE, true)
            setPackage(if (isPackageInstalled(simpleContacts)) simpleContacts else simpleContactsDebug)
            setDataAndType(
                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                "vnd.android.cursor.dir/person"
            )
            launchActivityIntent(this)
        }
    } else {
        ensureBackgroundThread {
            val lookupKey =
                SimpleContactsHelper(this).getContactLookupKey((contact).rawId.toString())
            val publicUri =
                Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
            runOnUiThread {
                launchViewContactIntent(publicUri)
            }
        }
    }
}

fun Activity.appLaunched(appId: String) {
    baseConfig.internalStoragePath = getInternalStoragePath()
    updateSDCardPath()
    baseConfig.appId = appId
    if (baseConfig.appRunCount == 0) {
        baseConfig.wasOrangeIconChecked = true
        checkAppIconColor()
    } else if (!baseConfig.wasOrangeIconChecked) {
        baseConfig.wasOrangeIconChecked = true
        val primaryColor = resources.getColor(R.color.color_primary)
        if (baseConfig.appIconColor != primaryColor) {
            getAppIconColors().forEachIndexed { index, color ->
                toggleAppIconColor(appId, index, color, false)
            }

            val defaultClassName =
                "${baseConfig.appId.removeSuffix(".debug")}.activities.SplashActivity"
            packageManager.setComponentEnabledSetting(
                ComponentName(baseConfig.appId, defaultClassName),
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP
            )

            val orangeClassName =
                "${baseConfig.appId.removeSuffix(".debug")}.activities.SplashActivity.Orange"
            packageManager.setComponentEnabledSetting(
                ComponentName(baseConfig.appId, orangeClassName),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            baseConfig.appIconColor = primaryColor
            baseConfig.lastIconColor = primaryColor
        }
    }

    baseConfig.appRunCount++
    if (baseConfig.appRunCount % 30 == 0 && !isAProApp()) {
        if (!resources.getBoolean(R.bool.hide_google_relations)) {
            showDonateOrUpgradeDialog()
        }
    }

    if (baseConfig.appRunCount % 40 == 0 && !baseConfig.wasAppRated) {
        if (!resources.getBoolean(R.bool.hide_google_relations)) {
            RateStarsDialog(this)
        }
    }
}

fun Activity.showDonateOrUpgradeDialog() {
    if (getCanAppBeUpgraded()) {
        UpgradeToProDialog(this)
    } else if (!isOrWasThankYouInstalled()) {
        DonateDialog(this)
    }
}

fun Activity.isAppInstalledOnSDCard(): Boolean = try {
    val applicationInfo = packageManager.getPackageInfo(packageName, 0).applicationInfo
    (applicationInfo.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE
} catch (e: Exception) {
    false
}

fun BaseSimpleActivity.isShowingAndroidSAFDialog(path: String): Boolean {
    return if (isRestrictedSAFOnlyRoot(path) && (getAndroidTreeUri(path).isEmpty() || !hasProperStoredAndroidTreeUri(
            path
        ))
    ) {
        runOnUiThread {
            if (!isDestroyed && !isFinishing) {
                ConfirmationAdvancedDialog(
                    this,
                    "",
                    R.string.confirm_storage_access_android_text,
                    R.string.ok,
                    R.string.cancel
                ) { success ->
                    if (success) {
                        Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                            putExtra(EXTRA_SHOW_ADVANCED, true)
                            putExtra(
                                DocumentsContract.EXTRA_INITIAL_URI,
                                createAndroidDataOrObbUri(path)
                            )
                            try {
                                startActivityForResult(
                                    this,
                                    OPEN_DOCUMENT_TREE_FOR_ANDROID_DATA_OR_OBB
                                )
                                checkedDocumentPath = path
                                return@apply
                            } catch (e: Exception) {
                                type = "*/*"
                            }

                            try {
                                startActivityForResult(
                                    this,
                                    OPEN_DOCUMENT_TREE_FOR_ANDROID_DATA_OR_OBB
                                )
                                checkedDocumentPath = path
                            } catch (e: ActivityNotFoundException) {
                                toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                            } catch (e: Exception) {
                                toast(R.string.unknown_error_occurred)
                            }
                        }
                    }
                }
            }
        }
        true
    } else {
        false
    }
}

fun Activity.launchPurchaseThankYouIntent() {
    hideKeyboard()
    try {
        launchViewIntent("market://details?id=com.simplemobiletools.thankyou")
    } catch (ignored: Exception) {
        launchViewIntent(getString(R.string.thank_you_url))
    }
}

fun Activity.launchUpgradeToProIntent() {
    hideKeyboard()
    try {
        launchViewIntent("market://details?id=${baseConfig.appId.removeSuffix(".debug")}.pro")
    } catch (ignored: Exception) {
        launchViewIntent(getStoreUrl())
    }
}

fun Activity.launchMoreAppsFromUsIntent() {
    launchViewIntent(DEVELOPER_PLAY_STORE_URL)
}

fun Activity.launchViewIntent(id: Int) = launchViewIntent(getString(id))

fun Activity.launchViewIntent(url: String) {
    hideKeyboard()
    ensureBackgroundThread {
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            try {
                startActivity(this)
            } catch (e: ActivityNotFoundException) {
                toast(R.string.no_browser_found)
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }
}

fun Activity.redirectToRateUs() {
    hideKeyboard()
    try {
        launchViewIntent("market://details?id=${packageName.removeSuffix(".debug")}")
    } catch (ignored: ActivityNotFoundException) {
        launchViewIntent(getStoreUrl())
    }
}

fun Activity.sharePathIntent(path: String, applicationId: String) {
    ensureBackgroundThread {
        val newUri = getFinalUriFromPath(path, applicationId) ?: return@ensureBackgroundThread
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(EXTRA_STREAM, newUri)
            type = getUriMimeType(path, newUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            grantUriPermission("android", newUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(Intent.createChooser(this, getString(R.string.share_via)))
            } catch (e: ActivityNotFoundException) {
                toast(R.string.no_app_found)
            } catch (e: RuntimeException) {
                if (e.cause is TransactionTooLargeException) {
                    toast(R.string.maximum_share_reached)
                } else {
                    showErrorToast(e)
                }
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }
}

fun Activity.sharePathsIntent(paths: List<String>, applicationId: String) {
    ensureBackgroundThread {
        if (paths.size == 1) {
            sharePathIntent(paths.first(), applicationId)
        } else {
            val uriPaths = ArrayList<String>()
            val newUris = paths.map {
                val uri = getFinalUriFromPath(it, applicationId) ?: return@ensureBackgroundThread
                uriPaths.add(uri.path!!)
                uri
            } as ArrayList<Uri>

            var mimeType = uriPaths.getMimeType()
            if (mimeType.isEmpty() || mimeType == "*/*") {
                mimeType = paths.getMimeType()
            }

            Intent().apply {
                action = Intent.ACTION_SEND_MULTIPLE
                type = mimeType
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putParcelableArrayListExtra(EXTRA_STREAM, newUris)

                try {
                    startActivity(Intent.createChooser(this, getString(R.string.share_via)))
                } catch (e: ActivityNotFoundException) {
                    toast(R.string.no_app_found)
                } catch (e: RuntimeException) {
                    if (e.cause is TransactionTooLargeException) {
                        toast(R.string.maximum_share_reached)
                    } else {
                        showErrorToast(e)
                    }
                } catch (e: Exception) {
                    showErrorToast(e)
                }
            }
        }
    }
}

fun Activity.setAsIntent(path: String, applicationId: String) {
    ensureBackgroundThread {
        val newUri = getFinalUriFromPath(path, applicationId) ?: return@ensureBackgroundThread
        Intent().apply {
            action = Intent.ACTION_ATTACH_DATA
            setDataAndType(newUri, getUriMimeType(path, newUri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val chooser = Intent.createChooser(this, getString(R.string.set_as))

            try {
                startActivityForResult(chooser, REQUEST_SET_AS)
            } catch (e: ActivityNotFoundException) {
                toast(R.string.no_app_found)
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }
}

fun Activity.shareTextIntent(text: String) {
    ensureBackgroundThread {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)

            try {
                startActivity(Intent.createChooser(this, getString(R.string.share_via)))
            } catch (e: ActivityNotFoundException) {
                toast(R.string.no_app_found)
            } catch (e: RuntimeException) {
                if (e.cause is TransactionTooLargeException) {
                    toast(R.string.maximum_share_reached)
                } else {
                    showErrorToast(e)
                }
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }
}

fun Activity.openEditorIntent(path: String, forceChooser: Boolean, applicationId: String) {
    ensureBackgroundThread {
        val newUri = getFinalUriFromPath(path, applicationId) ?: return@ensureBackgroundThread
        Intent().apply {
            action = Intent.ACTION_EDIT
            setDataAndType(newUri, getUriMimeType(path, newUri))
            if (!isRPlus() || (isRPlus() && (hasProperStoredDocumentUriSdk30(path) || Environment.isExternalStorageManager()))) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            val parent = path.getParentPath()
            val newFilename = "${path.getFilenameFromPath().substringBeforeLast('.')}_1"
            val extension = path.getFilenameExtension()
            val newFilePath = File(parent, "$newFilename.$extension")

            val outputUri = if (isPathOnOTG(path)) newUri else getFinalUriFromPath(
                "$newFilePath",
                applicationId
            )
            if (!isRPlus()) {
                val resInfoList =
                    packageManager.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY)
                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    grantUriPermission(
                        packageName,
                        outputUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }

            if (!isRPlus()) {
                putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            }

            putExtra(REAL_FILE_PATH, path)

            try {
                val chooser = Intent.createChooser(this, getString(R.string.edit_with))
                startActivityForResult(if (forceChooser) chooser else this, REQUEST_EDIT_IMAGE)
            } catch (e: ActivityNotFoundException) {
                toast(R.string.no_app_found)
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }
}

fun Activity.openPathIntent(
    path: String,
    forceChooser: Boolean,
    applicationId: String,
    forceMimeType: String = "",
    extras: HashMap<String, Boolean> = HashMap()
) {
    ensureBackgroundThread {
        val newUri = getFinalUriFromPath(path, applicationId) ?: return@ensureBackgroundThread
        val mimeType =
            if (forceMimeType.isNotEmpty()) forceMimeType else getUriMimeType(path, newUri)
        Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(newUri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            if (applicationId == "com.simplemobiletools.gallery.pro" || applicationId == "com.simplemobiletools.gallery.pro.debug") {
                putExtra(IS_FROM_GALLERY, true)
            }

            for ((key, value) in extras) {
                putExtra(key, value)
            }

            putExtra(REAL_FILE_PATH, path)

            try {
                val chooser = Intent.createChooser(this, getString(R.string.open_with))
                startActivity(if (forceChooser) chooser else this)
            } catch (e: ActivityNotFoundException) {
                if (!tryGenericMimeType(this, mimeType, newUri)) {
                    toast(R.string.no_app_found)
                }
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }
}

fun Activity.launchViewContactIntent(uri: Uri) {
    Intent().apply {
        action = ContactsContract.QuickContact.ACTION_QUICK_CONTACT
        data = uri
        launchActivityIntent(this)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.launchCallIntent(recipient: String, handle: PhoneAccountHandle? = null) {
    handlePermission(PERMISSION_CALL_PHONE) {
        val action = if (it) Intent.ACTION_CALL else Intent.ACTION_DIAL
        Intent(action).apply {
            data = Uri.fromParts("tel", recipient, null)

            if (handle != null) {
                putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
            }

            if (isDefaultDialer()) {
                val packageName = if (baseConfig.appId.contains(
                        ".debug",
                        true
                    )
                ) "com.simplemobiletools.dialer.debug" else "com.simplemobiletools.dialer"
                val className = "com.simplemobiletools.dialer.activities.DialerActivity"
                setClassName(packageName, className)
            }

            launchActivityIntent(this)
        }
    }
}

fun Activity.launchSendSMSIntent(recipient: String) {
    Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.fromParts("smsto", recipient, null)
        launchActivityIntent(this)
    }
}

fun Activity.showLocationOnMap(coordinates: String) {
    val uriBegin = "geo:${coordinates.replace(" ", "")}"
    val encodedQuery = Uri.encode(coordinates)
    val uriString = "$uriBegin?q=$encodedQuery&z=16"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
    launchActivityIntent(intent)
}

fun Activity.getFinalUriFromPath(path: String, applicationId: String): Uri? {
    val uri = try {
        ensurePublicUri(path, applicationId)
    } catch (e: Exception) {
        showErrorToast(e)
        return null
    }

    if (uri == null) {
        toast(R.string.unknown_error_occurred)
        return null
    }

    return uri
}

fun Activity.scanFilesRecursively(files: List<File>, callback: (() -> Unit)? = null) {
    applicationContext.scanFilesRecursively(files, callback)
}

fun Activity.tryGenericMimeType(intent: Intent, mimeType: String, uri: Uri): Boolean {
    var genericMimeType = mimeType.getGenericMimeType()
    if (genericMimeType.isEmpty()) {
        genericMimeType = "*/*"
    }

    intent.setDataAndType(uri, genericMimeType)

    return try {
        startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}

fun BaseSimpleActivity.checkWhatsNew(releases: List<Release>, currVersion: Int) {
    if (baseConfig.lastVersion == 0) {
        baseConfig.lastVersion = currVersion
        return
    }

    val newReleases = arrayListOf<Release>()
    releases.filterTo(newReleases) { it.id > baseConfig.lastVersion }

    if (newReleases.isNotEmpty()) {
        WhatsNewDialog(this, newReleases)
    }

    baseConfig.lastVersion = currVersion
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.deleteFolders(
    folders: List<FileDirItem>,
    deleteMediaOnly: Boolean = true,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    ensureBackgroundThread {
        deleteFoldersBg(folders, deleteMediaOnly, callback)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.deleteFoldersBg(
    folders: List<FileDirItem>,
    deleteMediaOnly: Boolean = true,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    var wasSuccess = false
    var needPermissionForPath = ""
    for (folder in folders) {
        if (needsStupidWritePermissions(folder.path) && baseConfig.sdTreeUri.isEmpty()) {
            needPermissionForPath = folder.path
            break
        }
    }

    handleSAFDialog(needPermissionForPath) {
        if (!it) {
            return@handleSAFDialog
        }

        folders.forEachIndexed { index, folder ->
            deleteFolderBg(folder, deleteMediaOnly) {
                if (it)
                    wasSuccess = true

                if (index == folders.size - 1) {
                    runOnUiThread {
                        callback?.invoke(wasSuccess)
                    }
                }
            }
        }
    }
}

fun BaseSimpleActivity.deleteFolder(
    folder: FileDirItem,
    deleteMediaOnly: Boolean = true,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    ensureBackgroundThread {
        deleteFolderBg(folder, deleteMediaOnly, callback)
    }
}

fun BaseSimpleActivity.deleteFolderBg(
    fileDirItem: FileDirItem,
    deleteMediaOnly: Boolean = true,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    val folder = File(fileDirItem.path)
    if (folder.exists()) {
        val filesArr = folder.listFiles()
        if (filesArr == null) {
            runOnUiThread {
                callback?.invoke(true)
            }
            return
        }

        val files = filesArr.toMutableList().filter { !deleteMediaOnly || it.isMediaFile() }
        for (file in files) {
            deleteFileBg(
                file.toFileDirItem(applicationContext),
                allowDeleteFolder = false,
                isDeletingMultipleFiles = false
            ) { }
        }

        if (folder.listFiles()?.isEmpty() == true) {
            deleteFileBg(fileDirItem, allowDeleteFolder = true, isDeletingMultipleFiles = false) { }
        }
    }
    runOnUiThread {
        callback?.invoke(true)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.deleteFile(
    file: FileDirItem,
    allowDeleteFolder: Boolean = false,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    deleteFiles(arrayListOf(file), allowDeleteFolder, callback)
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.deleteFiles(
    files: List<FileDirItem>,
    allowDeleteFolder: Boolean = false,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    ensureBackgroundThread {
        deleteFilesBg(files, allowDeleteFolder, callback)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.deleteFilesBg(
    files: List<FileDirItem>,
    allowDeleteFolder: Boolean = false,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    if (files.isEmpty()) {
        runOnUiThread {
            callback?.invoke(true)
        }
        return
    }

    val firstFile = files.first()
    val firstFilePath = firstFile.path
    handleSAFDialog(firstFilePath) {
        if (!it) {
            return@handleSAFDialog
        }

        checkManageMediaOrHandleSAFDialogSdk30(firstFilePath) {
            if (!it) {
                return@checkManageMediaOrHandleSAFDialogSdk30
            }

            val recycleBinPath = firstFile.isRecycleBinPath(this)
            if (canManageMedia() && !recycleBinPath && !firstFilePath.doesThisOrParentHaveNoMedia(
                    HashMap(), null
                )
            ) {
                val fileUris = getFileUrisFromFileDirItems(files)

                deleteSDK30Uris(fileUris) { success ->
                    runOnUiThread {
                        callback?.invoke(success)
                    }
                }
            } else {
                deleteFilesCasual(files, allowDeleteFolder, callback)
            }
        }
    }
}

private fun BaseSimpleActivity.deleteFilesCasual(
    files: List<FileDirItem>,
    allowDeleteFolder: Boolean = false,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    var wasSuccess = false
    val failedFileDirItems = ArrayList<FileDirItem>()
    files.forEachIndexed { index, file ->
        deleteFileBg(file, allowDeleteFolder, true) {
            if (it) {
                wasSuccess = true
            } else {
                failedFileDirItems.add(file)
            }

            if (index == files.lastIndex) {
                if (isRPlus() && failedFileDirItems.isNotEmpty()) {
                    val fileUris = getFileUrisFromFileDirItems(failedFileDirItems)
                    deleteSDK30Uris(fileUris) { success ->
                        runOnUiThread {
                            callback?.invoke(success)
                        }
                    }
                } else {
                    runOnUiThread {
                        callback?.invoke(wasSuccess)
                    }
                }
            }
        }
    }
}

fun BaseSimpleActivity.deleteFile(
    fileDirItem: FileDirItem,
    allowDeleteFolder: Boolean = false,
    isDeletingMultipleFiles: Boolean,
    callback: ((wasSuccess: Boolean) -> Unit)? = null
) {
    ensureBackgroundThread {
        deleteFileBg(fileDirItem, allowDeleteFolder, isDeletingMultipleFiles, callback)
    }
}

fun BaseSimpleActivity.deleteFileBg(
    fileDirItem: FileDirItem,
    allowDeleteFolder: Boolean = false,
    isDeletingMultipleFiles: Boolean,
    callback: ((wasSuccess: Boolean) -> Unit)? = null,
) {
    val path = fileDirItem.path
    if (isRestrictedSAFOnlyRoot(path)) {
        deleteAndroidSAFDirectory(path, allowDeleteFolder, callback)
    } else {
        val file = File(path)
        if (!isRPlus() && file.absolutePath.startsWith(internalStoragePath) && !file.canWrite()) {
            callback?.invoke(false)
            return
        }

        var fileDeleted =
            !isPathOnOTG(path) && ((!file.exists() && file.length() == 0L) || file.delete())
        if (fileDeleted) {
            deleteFromMediaStore(path) { needsRescan ->
                if (needsRescan) {
                    rescanAndDeletePath(path) {
                        runOnUiThread {
                            callback?.invoke(true)
                        }
                    }
                } else {
                    runOnUiThread {
                        callback?.invoke(true)
                    }
                }
            }
        } else {
            if (getIsPathDirectory(file.absolutePath) && allowDeleteFolder) {
                fileDeleted = deleteRecursively(file, this)
            }

            if (!fileDeleted) {
                if (needsStupidWritePermissions(path)) {
                    handleSAFDialog(path) {
                        if (it) {
                            trySAFFileDelete(fileDirItem, allowDeleteFolder, callback)
                        }
                    }
                } else if (isAccessibleWithSAFSdk30(path)) {
                    if (canManageMedia()) {
                        deleteSdk30(fileDirItem, callback)
                    } else {
                        handleSAFDialogSdk30(path) {
                            if (it) {
                                deleteDocumentWithSAFSdk30(fileDirItem, allowDeleteFolder, callback)
                            }
                        }
                    }
                } else if (isRPlus() && !isDeletingMultipleFiles) {
                    deleteSdk30(fileDirItem, callback)
                } else {
                    callback?.invoke(false)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun BaseSimpleActivity.deleteSdk30(
    fileDirItem: FileDirItem,
    callback: ((wasSuccess: Boolean) -> Unit)?
) {
    val fileUris = getFileUrisFromFileDirItems(arrayListOf(fileDirItem))
    deleteSDK30Uris(fileUris) { success ->
        runOnUiThread {
            callback?.invoke(success)
        }
    }
}

private fun deleteRecursively(file: File, context: Context): Boolean {
    if (file.isDirectory) {
        val files = file.listFiles() ?: return file.delete()
        for (child in files) {
            deleteRecursively(child, context)
        }
    }

    val deleted = file.delete()
    if (deleted) {
        context.deleteFromMediaStore(file.absolutePath)
    }
    return deleted
}

fun Activity.scanPathsRecursively(paths: List<String>, callback: (() -> Unit)? = null) {
    applicationContext.scanPathsRecursively(paths, callback)
}

fun Activity.createTempFile(file: File): File? {
    return if (file.isDirectory) {
        createTempDir("temp", "${System.currentTimeMillis()}", file.parentFile)
    } else {
        if (isRPlus()) {
            // this can throw FileSystemException, lets catch and handle it at the place calling this function
            kotlin.io.path.createTempFile(
                file.parentFile.toPath(),
                "temp",
                "${System.currentTimeMillis()}"
            ).toFile()
        } else {
            createTempFile("temp", "${System.currentTimeMillis()}", file.parentFile)
        }
    }
}

fun Activity.hideKeyboard() {
    if (isOnMainThread()) {
        hideKeyboardSync()
    } else {
        Handler(Looper.getMainLooper()).post {
            hideKeyboardSync()
        }
    }
}

fun Activity.hideKeyboardSync() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    currentFocus?.clearFocus()
}

fun Activity.showKeyboard(et: EditText) {
    et.requestFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.getFileOutputStream(
    fileDirItem: FileDirItem,
    allowCreatingNewFile: Boolean = false,
    callback: (outputStream: OutputStream?) -> Unit
) {
    val targetFile = File(fileDirItem.path)
    when {
        isRestrictedSAFOnlyRoot(fileDirItem.path) -> {
            handleAndroidSAFDialog(fileDirItem.path) {
                if (!it) {
                    return@handleAndroidSAFDialog
                }

                val uri = getAndroidSAFUri(fileDirItem.path)
                if (!getDoesFilePathExist(fileDirItem.path)) {
                    createAndroidSAFFile(fileDirItem.path)
                }
                callback.invoke(applicationContext.contentResolver.openOutputStream(uri, "wt"))
            }
        }

        needsStupidWritePermissions(fileDirItem.path) -> {
            handleSAFDialog(fileDirItem.path) {
                if (!it) {
                    return@handleSAFDialog
                }

                var document = getDocumentFile(fileDirItem.path)
                if (document == null && allowCreatingNewFile) {
                    document = getDocumentFile(fileDirItem.getParentPath())
                }

                if (document == null) {
                    showFileCreateError(fileDirItem.path)
                    callback(null)
                    return@handleSAFDialog
                }

                if (!getDoesFilePathExist(fileDirItem.path)) {
                    document = getDocumentFile(fileDirItem.path) ?: document.createFile(
                        "",
                        fileDirItem.name
                    )
                }

                if (document?.exists() == true) {
                    try {
                        callback(
                            applicationContext.contentResolver.openOutputStream(
                                document.uri,
                                "wt"
                            )
                        )
                    } catch (e: FileNotFoundException) {
                        showErrorToast(e)
                        callback(null)
                    }
                } else {
                    showFileCreateError(fileDirItem.path)
                    callback(null)
                }
            }
        }

        isAccessibleWithSAFSdk30(fileDirItem.path) -> {
            handleSAFDialogSdk30(fileDirItem.path) {
                if (!it) {
                    return@handleSAFDialogSdk30
                }

                callback.invoke(
                    try {
                        val uri = createDocumentUriUsingFirstParentTreeUri(fileDirItem.path)
                        if (!getDoesFilePathExist(fileDirItem.path)) {
                            createSAFFileSdk30(fileDirItem.path)
                        }
                        applicationContext.contentResolver.openOutputStream(uri, "wt")
                    } catch (e: Exception) {
                        null
                    } ?: createCasualFileOutputStream(this, targetFile)
                )
            }
        }

        isRestrictedWithSAFSdk30(fileDirItem.path) -> {
            callback.invoke(
                try {
                    val fileUri = getFileUrisFromFileDirItems(arrayListOf(fileDirItem))
                    applicationContext.contentResolver.openOutputStream(fileUri.first(), "wt")
                } catch (e: Exception) {
                    null
                } ?: createCasualFileOutputStream(this, targetFile)
            )
        }

        else -> {
            callback.invoke(createCasualFileOutputStream(this, targetFile))
        }
    }
}

private fun createCasualFileOutputStream(
    activity: BaseSimpleActivity,
    targetFile: File
): OutputStream? {
    if (targetFile.parentFile?.exists() == false) {
        targetFile.parentFile?.mkdirs()
    }

    return try {
        FileOutputStream(targetFile)
    } catch (e: Exception) {
        activity.showErrorToast(e)
        null
    }
}

fun Activity.handleHiddenFolderPasswordProtection(callback: () -> Unit) {
    if (baseConfig.isHiddenPasswordProtectionOn) {
        SecurityDialog(
            this,
            baseConfig.hiddenPasswordHash,
            baseConfig.hiddenProtectionType
        ) { _, _, success ->
            if (success) {
                callback()
            }
        }
    } else {
        callback()
    }
}

fun Activity.handleAppPasswordProtection(callback: (success: Boolean) -> Unit) {
    if (baseConfig.isAppPasswordProtectionOn) {
        SecurityDialog(
            this,
            baseConfig.appPasswordHash,
            baseConfig.appProtectionType
        ) { _, _, success ->
            callback(success)
        }
    } else {
        callback(true)
    }
}

fun Activity.handleDeletePasswordProtection(callback: () -> Unit) {
    if (baseConfig.isDeletePasswordProtectionOn) {
        SecurityDialog(
            this,
            baseConfig.deletePasswordHash,
            baseConfig.deleteProtectionType
        ) { _, _, success ->
            if (success) {
                callback()
            }
        }
    } else {
        callback()
    }
}

fun Activity.handleLockedFolderOpening(path: String, callback: (success: Boolean) -> Unit) {
    if (baseConfig.isFolderProtected(path)) {
        SecurityDialog(
            this,
            baseConfig.getFolderProtectionHash(path),
            baseConfig.getFolderProtectionType(path)
        ) { _, _, success ->
            callback(success)
        }
    } else {
        callback(true)
    }
}

fun Activity.updateSharedTheme(sharedTheme: SharedTheme) {
    try {
        val contentValues = MyContentProvider.fillThemeContentValues(sharedTheme)
        applicationContext.contentResolver.update(
            MyContentProvider.MY_CONTENT_URI,
            contentValues,
            null,
            null
        )
    } catch (e: Exception) {
        showErrorToast(e)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Activity.setupDialogStuff(
    view: View,
    dialog: AlertDialog.Builder,
    titleId: Int = 0,
    titleText: String = "",
    cancelOnTouchOutside: Boolean = true,
    callback: ((alertDialog: AlertDialog) -> Unit)? = null
) {
    if (isDestroyed || isFinishing) {
        return
    }

    val textColor = getProperTextColor()
    val backgroundColor = getProperBackgroundColor()
    val primaryColor = getProperPrimaryColor()
    if (view is ViewGroup) {
        updateTextColors(view)
    } else if (view is MyTextView) {
        view.setColors(textColor, primaryColor, backgroundColor)
    }

    if (dialog is MaterialAlertDialogBuilder) {
        dialog.create().apply {
            if (titleId != 0) {
                setTitle(titleId)
            } else if (titleText.isNotEmpty()) {
                setTitle(titleText)
            }

            setView(view)
            setCancelable(cancelOnTouchOutside)
            if (!isFinishing) {
                show()
            }
            getButton(Dialog.BUTTON_POSITIVE)?.setTextColor(primaryColor)
            getButton(Dialog.BUTTON_NEGATIVE)?.setTextColor(primaryColor)
            getButton(Dialog.BUTTON_NEUTRAL)?.setTextColor(primaryColor)
            callback?.invoke(this)
        }
    } else {
        var title: DialogTitleBinding? = null
        if (titleId != 0 || titleText.isNotEmpty()) {
            title = DialogTitleBinding.inflate(layoutInflater, null, false)
            title.dialogTitleTextview.apply {
                if (titleText.isNotEmpty()) {
                    text = titleText
                } else {
                    setText(titleId)
                }
                setTextColor(textColor)
            }
        }

        // if we use the same primary and background color, use the text color for dialog confirmation buttons
        val dialogButtonColor = if (primaryColor == baseConfig.backgroundColor) {
            textColor
        } else {
            primaryColor
        }

        dialog.create().apply {
            setView(view)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCustomTitle(title?.root)
            setCanceledOnTouchOutside(cancelOnTouchOutside)
            if (!isFinishing) {
                show()
            }
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(dialogButtonColor)
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(dialogButtonColor)
            getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(dialogButtonColor)

            val bgDrawable = when {
                isBlackAndWhiteTheme() -> resources.getDrawable(
                    R.drawable.black_dialog_background,
                    theme
                )

                baseConfig.isUsingSystemTheme -> resources.getDrawable(
                    R.drawable.dialog_you_background,
                    theme
                )

                else -> resources.getColoredDrawableWithColor(
                    R.drawable.dialog_bg,
                    baseConfig.backgroundColor
                )
            }

            window?.setBackgroundDrawable(bgDrawable)
            callback?.invoke(this)
        }
    }
}

fun Activity.getAlertDialogBuilder() = if (baseConfig.isUsingSystemTheme) {
    MaterialAlertDialogBuilder(this)
} else {
    AlertDialog.Builder(this)
}

fun Activity.showPickSecondsDialogHelper(
    curMinutes: Int,
    isSnoozePicker: Boolean = false,
    showSecondsAtCustomDialog: Boolean = false,
    showDuringDayOption: Boolean = false,
    cancelCallback: (() -> Unit)? = null,
    callback: (seconds: Int) -> Unit
) {
    val seconds = if (curMinutes == -1) curMinutes else curMinutes * 60
    showPickSecondsDialog(
        seconds,
        isSnoozePicker,
        showSecondsAtCustomDialog,
        showDuringDayOption,
        cancelCallback,
        callback
    )
}

fun Activity.showPickSecondsDialog(
    curSeconds: Int,
    isSnoozePicker: Boolean = false,
    showSecondsAtCustomDialog: Boolean = false,
    showDuringDayOption: Boolean = false,
    cancelCallback: (() -> Unit)? = null,
    callback: (seconds: Int) -> Unit
) {
    hideKeyboard()
    val seconds = TreeSet<Int>()
    seconds.apply {
        if (!isSnoozePicker) {
            add(-1)
            add(0)
        }
        add(1 * MINUTE_SECONDS)
        add(5 * MINUTE_SECONDS)
        add(10 * MINUTE_SECONDS)
        add(30 * MINUTE_SECONDS)
        add(60 * MINUTE_SECONDS)
        add(curSeconds)
    }

    val items = ArrayList<RadioItem>(seconds.size + 1)
    seconds.mapIndexedTo(items) { index, value ->
        RadioItem(index, getFormattedSeconds(value, !isSnoozePicker), value)
    }

    var selectedIndex = 0
    seconds.forEachIndexed { index, value ->
        if (value == curSeconds) {
            selectedIndex = index
        }
    }

    items.add(RadioItem(-2, getString(R.string.custom)))

    if (showDuringDayOption) {
        items.add(RadioItem(-3, getString(R.string.during_day_at_hh_mm)))
    }

    RadioGroupDialog(
        this,
        items,
        selectedIndex,
        showOKButton = isSnoozePicker,
        cancelCallback = cancelCallback
    ) {
        when (it) {
            -2 -> {
                CustomIntervalPickerDialog(this, showSeconds = showSecondsAtCustomDialog) {
                    callback(it)
                }
            }

            -3 -> {
                TimePickerDialog(
                    this, getTimePickerDialogTheme(),
                    { view, hourOfDay, minute -> callback(hourOfDay * -3600 + minute * -60) },
                    curSeconds / 3600, curSeconds % 3600, baseConfig.use24HourFormat
                ).show()
            }

            else -> {
                callback(it as Int)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.getAlarmSounds(type: Int, callback: (ArrayList<AlarmSound>) -> Unit) {
    val alarms = ArrayList<AlarmSound>()
    val manager = RingtoneManager(this)
    manager.setType(type)

    try {
        val cursor = manager.cursor
        var curId = 1
        val silentAlarm = AlarmSound(curId++, getString(R.string.no_sound), SILENT)
        alarms.add(silentAlarm)

        while (cursor.moveToNext()) {
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            var uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
            val id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
            if (!uri.endsWith(id)) {
                uri += "/$id"
            }

            val alarmSound = AlarmSound(curId++, title, uri)
            alarms.add(alarmSound)
        }
        callback(alarms)
    } catch (e: Exception) {
        if (e is SecurityException) {
            handlePermission(PERMISSION_READ_STORAGE) {
                if (it) {
                    getAlarmSounds(type, callback)
                } else {
                    showErrorToast(e)
                    callback(ArrayList())
                }
            }
        } else {
            showErrorToast(e)
            callback(ArrayList())
        }
    }
}

fun Activity.checkAppSideloading(): Boolean {
    val isSideloaded = when (baseConfig.appSideloadingStatus) {
        SIDELOADING_TRUE -> true
        SIDELOADING_FALSE -> false
        else -> isAppSideloaded()
    }

    baseConfig.appSideloadingStatus = if (isSideloaded) SIDELOADING_TRUE else SIDELOADING_FALSE
    if (isSideloaded) {
        showSideloadingDialog()
    }

    return isSideloaded
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Activity.isAppSideloaded(): Boolean {
    return try {
        getDrawable(R.drawable.ic_camera_vector)
        false
    } catch (e: Exception) {
        true
    }
}

fun Activity.showSideloadingDialog() {
    AppSideloadedDialog(this) {
        finish()
    }
}

fun Activity.onApplyWindowInsets(callback: (WindowInsetsCompat) -> Unit) {
    window.decorView.setOnApplyWindowInsetsListener { view, insets ->
        callback(WindowInsetsCompat.toWindowInsetsCompat(insets))
        view.onApplyWindowInsets(insets)
        insets
    }
}

fun BaseSimpleActivity.copySingleFileSdk30(source: FileDirItem, destination: FileDirItem): Boolean {
    val directory = destination.getParentPath()
    if (!createDirectorySync(directory)) {
        val error = String.format(getString(R.string.could_not_create_folder), directory)
        showErrorToast(error)
        return false
    }

    var inputStream: InputStream? = null
    var out: OutputStream? = null
    try {

        out = getFileOutputStreamSync(destination.path, source.path.getMimeType())
        inputStream = getFileInputStreamSync(source.path)!!

        var copiedSize = 0L
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = inputStream.read(buffer)
        while (bytes >= 0) {
            out!!.write(buffer, 0, bytes)
            copiedSize += bytes
            bytes = inputStream.read(buffer)
        }

        out?.flush()

        return if (source.size == copiedSize && getDoesFilePathExist(destination.path)) {
            if (baseConfig.keepLastModified) {
                copyOldLastModified(source.path, destination.path)
                val lastModified = File(source.path).lastModified()
                if (lastModified != 0L) {
                    File(destination.path).setLastModified(lastModified)
                }
            }
            true
        } else {
            false
        }
    } finally {
        inputStream?.close()
        out?.close()
    }
}

fun BaseSimpleActivity.copyOldLastModified(sourcePath: String, destinationPath: String) {
    val projection =
        arrayOf(MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATE_MODIFIED)
    val uri = MediaStore.Files.getContentUri("external")
    val selection = "${MediaStore.MediaColumns.DATA} = ?"
    var selectionArgs = arrayOf(sourcePath)
    val cursor =
        applicationContext.contentResolver.query(uri, projection, selection, selectionArgs, null)

    cursor?.use {
        if (cursor.moveToFirst()) {
            val dateTaken = cursor.getLongValue(MediaStore.Images.Media.DATE_TAKEN)
            val dateModified = cursor.getIntValue(MediaStore.Images.Media.DATE_MODIFIED)

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DATE_TAKEN, dateTaken)
                put(MediaStore.Images.Media.DATE_MODIFIED, dateModified)
            }

            selectionArgs = arrayOf(destinationPath)
            applicationContext.contentResolver.update(uri, values, selection, selectionArgs)
        }
    }
}

inline fun <T : ViewBinding> Activity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.isShowingSAFDialog(path: String): Boolean {
    return if ((!isRPlus() && isPathOnSD(path) && !isSDCardSetAsDefaultStorage() && (baseConfig.sdTreeUri.isEmpty() || !hasProperStoredTreeUri(
            false
        )))
    ) {
        runOnUiThread {
            if (!isDestroyed && !isFinishing) {
                com.simplemobiletools.smsmessenger.dialogs.WritePermissionDialog(
                    this,
                    com.simplemobiletools.smsmessenger.dialogs.WritePermissionDialog.WritePermissionDialogMode.SdCard
                ) {
                    Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                        putExtra(EXTRA_SHOW_ADVANCED, true)
                        try {
                            startActivityForResult(this, OPEN_DOCUMENT_TREE_SD)
                            checkedDocumentPath = path
                            return@apply
                        } catch (e: Exception) {
                            type = "*/*"
                        }

                        try {
                            startActivityForResult(this, OPEN_DOCUMENT_TREE_SD)
                            checkedDocumentPath = path
                        } catch (e: ActivityNotFoundException) {
                            toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                        } catch (e: Exception) {
                            toast(R.string.unknown_error_occurred)
                        }
                    }
                }
            }
        }
        true
    } else {
        false
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.isShowingOTGDialog(path: String): Boolean {
    return if (!isRPlus() && isPathOnOTG(path) && (baseConfig.OTGTreeUri.isEmpty() || !hasProperStoredTreeUri(
            true
        ))
    ) {
        showOTGPermissionDialog(path)
        true
    } else {
        false
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun BaseSimpleActivity.showOTGPermissionDialog(path: String) {
    runOnUiThread {
        if (!isDestroyed && !isFinishing) {
            com.simplemobiletools.smsmessenger.dialogs.WritePermissionDialog(
                this,
                com.simplemobiletools.smsmessenger.dialogs.WritePermissionDialog.WritePermissionDialogMode.Otg
            ) {
                Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                    try {
                        startActivityForResult(this, OPEN_DOCUMENT_TREE_OTG)
                        checkedDocumentPath = path
                        return@apply
                    } catch (e: Exception) {
                        type = "*/*"
                    }

                    try {
                        startActivityForResult(this, OPEN_DOCUMENT_TREE_OTG)
                        checkedDocumentPath = path
                    } catch (e: ActivityNotFoundException) {
                        toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                    } catch (e: Exception) {
                        toast(R.string.unknown_error_occurred)
                    }
                }
            }
        }
    }
}

@SuppressLint("InlinedApi")
fun BaseSimpleActivity.isShowingSAFDialogSdk30(path: String): Boolean {
    return if (isAccessibleWithSAFSdk30(path) && !hasProperStoredFirstParentUri(path)) {
        runOnUiThread {
            if (!isDestroyed && !isFinishing) {
                val level = getFirstParentLevel(path)
                com.simplemobiletools.smsmessenger.dialogs.WritePermissionDialog(
                    this,
                    com.simplemobiletools.smsmessenger.dialogs.WritePermissionDialog.WritePermissionDialogMode.OpenDocumentTreeSDK30(
                        path.getFirstParentPath(this, level)
                    )
                ) {
                    Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                        putExtra(EXTRA_SHOW_ADVANCED, true)
                        putExtra(
                            DocumentsContract.EXTRA_INITIAL_URI,
                            createFirstParentTreeUriUsingRootTree(path)
                        )
                        try {
                            startActivityForResult(this, OPEN_DOCUMENT_TREE_FOR_SDK_30)
                            checkedDocumentPath = path
                            return@apply
                        } catch (e: Exception) {
                            type = "*/*"
                        }

                        try {
                            startActivityForResult(this, OPEN_DOCUMENT_TREE_FOR_SDK_30)
                            checkedDocumentPath = path
                        } catch (e: ActivityNotFoundException) {
                            toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                        } catch (e: Exception) {
                            toast(R.string.unknown_error_occurred)
                        }
                    }
                }
            }
        }
        true
    } else {
        false
    }
}

@SuppressLint("InlinedApi")
fun BaseSimpleActivity.isShowingSAFCreateDocumentDialogSdk30(path: String): Boolean {
    return if (!hasProperStoredDocumentUriSdk30(path)) {
        runOnUiThread {
            if (!isDestroyed && !isFinishing) {
                com.simplemobiletools.smsmessenger.dialogs.WritePermissionDialog(
                    this,
                    com.simplemobiletools.smsmessenger.dialogs.WritePermissionDialog.WritePermissionDialogMode.CreateDocumentSDK30
                ) {
                    Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        type = DocumentsContract.Document.MIME_TYPE_DIR
                        putExtra(EXTRA_SHOW_ADVANCED, true)
                        addCategory(Intent.CATEGORY_OPENABLE)
                        putExtra(
                            DocumentsContract.EXTRA_INITIAL_URI,
                            buildDocumentUriSdk30(path.getParentPath())
                        )
                        putExtra(Intent.EXTRA_TITLE, path.getFilenameFromPath())
                        try {
                            startActivityForResult(this, CREATE_DOCUMENT_SDK_30)
                            checkedDocumentPath = path
                            return@apply
                        } catch (e: Exception) {
                            type = "*/*"
                        }

                        try {
                            startActivityForResult(this, CREATE_DOCUMENT_SDK_30)
                            checkedDocumentPath = path
                        } catch (e: ActivityNotFoundException) {
                            toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                        } catch (e: Exception) {
                            toast(R.string.unknown_error_occurred)
                        }
                    }
                }
            }
        }
        true
    } else {
        false
    }
}

fun Activity.rescanPath(path: String, callback: (() -> Unit)? = null) {
    applicationContext.rescanPath(path, callback)
}

fun Activity.getThemeId(color: Int = baseConfig.primaryColor, showTransparentTop: Boolean = false) =
    when {
        baseConfig.isUsingSystemTheme -> if (isUsingSystemDarkTheme()) R.style.AppTheme_Base_System else R.style.AppTheme_Base_System_Light
        isBlackAndWhiteTheme() -> when {
            showTransparentTop -> R.style.AppTheme_BlackAndWhite_NoActionBar
            baseConfig.primaryColor.getContrastColor() == DARK_GREY -> R.style.AppTheme_BlackAndWhite_DarkTextColor
            else -> R.style.AppTheme_BlackAndWhite
        }

        isWhiteTheme() -> when {
            showTransparentTop -> R.style.AppTheme_White_NoActionBar
            baseConfig.primaryColor.getContrastColor() == Color.WHITE -> R.style.AppTheme_White_LightTextColor
            else -> R.style.AppTheme_White
        }

        showTransparentTop -> {
            when (color) {
                -12846 -> R.style.AppTheme_Red_100_core
                -1074534 -> R.style.AppTheme_Red_200_core
                -1739917 -> R.style.AppTheme_Red_300_core
                -1092784 -> R.style.AppTheme_Red_400_core
                -769226 -> R.style.AppTheme_Red_500_core
                -1754827 -> R.style.AppTheme_Red_600_core
                -2937041 -> R.style.AppTheme_Red_700_core
                -3790808 -> R.style.AppTheme_Red_800_core
                -4776932 -> R.style.AppTheme_Red_900_core

                -476208 -> R.style.AppTheme_Pink_100_core
                -749647 -> R.style.AppTheme_Pink_200_core
                -1023342 -> R.style.AppTheme_Pink_300_core
                -1294214 -> R.style.AppTheme_Pink_400_core
                -1499549 -> R.style.AppTheme_Pink_500_core
                -2614432 -> R.style.AppTheme_Pink_600_core
                -4056997 -> R.style.AppTheme_Pink_700_core
                -5434281 -> R.style.AppTheme_Pink_800_core
                -7860657 -> R.style.AppTheme_Pink_900_core

                -1982745 -> R.style.AppTheme_Purple_100_core
                -3238952 -> R.style.AppTheme_Purple_200_core
                -4560696 -> R.style.AppTheme_Purple_300_core
                -5552196 -> R.style.AppTheme_Purple_400_core
                -6543440 -> R.style.AppTheme_Purple_500_core
                -7461718 -> R.style.AppTheme_Purple_600_core
                -8708190 -> R.style.AppTheme_Purple_700_core
                -9823334 -> R.style.AppTheme_Purple_800_core
                -11922292 -> R.style.AppTheme_Purple_900_core

                -3029783 -> R.style.AppTheme_Deep_Purple_100_core
                -5005861 -> R.style.AppTheme_Deep_Purple_200_core
                -6982195 -> R.style.AppTheme_Deep_Purple_300_core
                -8497214 -> R.style.AppTheme_Deep_Purple_400_core
                -10011977 -> R.style.AppTheme_Deep_Purple_500_core
                -10603087 -> R.style.AppTheme_Deep_Purple_600_core
                -11457112 -> R.style.AppTheme_Deep_Purple_700_core
                -12245088 -> R.style.AppTheme_Deep_Purple_800_core
                -13558894 -> R.style.AppTheme_Deep_Purple_900_core

                -3814679 -> R.style.AppTheme_Indigo_100_core
                -6313766 -> R.style.AppTheme_Indigo_200_core
                -8812853 -> R.style.AppTheme_Indigo_300_core
                -10720320 -> R.style.AppTheme_Indigo_400_core
                -12627531 -> R.style.AppTheme_Indigo_500_core
                -13022805 -> R.style.AppTheme_Indigo_600_core
                -13615201 -> R.style.AppTheme_Indigo_700_core
                -14142061 -> R.style.AppTheme_Indigo_800_core
                -15064194 -> R.style.AppTheme_Indigo_900_core

                -4464901 -> R.style.AppTheme_Blue_100_core
                -7288071 -> R.style.AppTheme_Blue_200_core
                -10177034 -> R.style.AppTheme_Blue_300_core
                -12409355 -> R.style.AppTheme_Blue_400_core
                -14575885 -> R.style.AppTheme_Blue_500_core
                -14776091 -> R.style.AppTheme_Blue_600_core
                -15108398 -> R.style.AppTheme_Blue_700_core
                -15374912 -> R.style.AppTheme_Blue_800_core
                -15906911 -> R.style.AppTheme_Blue_900_core

                -4987396 -> R.style.AppTheme_Light_Blue_100_core
                -8268550 -> R.style.AppTheme_Light_Blue_200_core
                -11549705 -> R.style.AppTheme_Light_Blue_300_core
                -14043396 -> R.style.AppTheme_Light_Blue_400_core
                -16537100 -> R.style.AppTheme_Light_Blue_500_core
                -16540699 -> R.style.AppTheme_Light_Blue_600_core
                -16611119 -> R.style.AppTheme_Light_Blue_700_core
                -16615491 -> R.style.AppTheme_Light_Blue_800_core
                -16689253 -> R.style.AppTheme_Light_Blue_900_core

                -5051406 -> R.style.AppTheme_Cyan_100_core
                -8331542 -> R.style.AppTheme_Cyan_200_core
                -11677471 -> R.style.AppTheme_Cyan_300_core
                -14235942 -> R.style.AppTheme_Cyan_400_core
                -16728876 -> R.style.AppTheme_Cyan_500_core
                -16732991 -> R.style.AppTheme_Cyan_600_core
                -16738393 -> R.style.AppTheme_Cyan_700_core
                -16743537 -> R.style.AppTheme_Cyan_800_core
                -16752540 -> R.style.AppTheme_Cyan_900_core

                -5054501 -> R.style.AppTheme_Teal_100_core
                -8336444 -> R.style.AppTheme_Teal_200_core
                -11684180 -> R.style.AppTheme_Teal_300_core
                -14244198 -> R.style.AppTheme_Teal_400_core
                -16738680 -> R.style.AppTheme_Teal_500_core
                -16742021 -> R.style.AppTheme_Teal_600_core
                -16746133 -> R.style.AppTheme_Teal_700_core
                -16750244 -> R.style.AppTheme_Teal_800_core
                -16757440 -> R.style.AppTheme_Teal_900_core

                -3610935 -> R.style.AppTheme_Green_100_core
                -5908825 -> R.style.AppTheme_Green_200_core
                -8271996 -> R.style.AppTheme_Green_300_core
                -10044566 -> R.style.AppTheme_Green_400_core
                -11751600 -> R.style.AppTheme_Green_500_core
                -12345273 -> R.style.AppTheme_Green_600_core
                -13070788 -> R.style.AppTheme_Green_700_core
                -13730510 -> R.style.AppTheme_Green_800_core
                -14983648 -> R.style.AppTheme_Green_900_core

                -2298424 -> R.style.AppTheme_Light_Green_100_core
                -3808859 -> R.style.AppTheme_Light_Green_200_core
                -5319295 -> R.style.AppTheme_Light_Green_300_core
                -6501275 -> R.style.AppTheme_Light_Green_400_core
                -7617718 -> R.style.AppTheme_Light_Green_500_core
                -8604862 -> R.style.AppTheme_Light_Green_600_core
                -9920712 -> R.style.AppTheme_Light_Green_700_core
                -11171025 -> R.style.AppTheme_Light_Green_800_core
                -13407970 -> R.style.AppTheme_Light_Green_900_core

                -985917 -> R.style.AppTheme_Lime_100_core
                -1642852 -> R.style.AppTheme_Lime_200_core
                -2300043 -> R.style.AppTheme_Lime_300_core
                -2825897 -> R.style.AppTheme_Lime_400_core
                -3285959 -> R.style.AppTheme_Lime_500_core
                -4142541 -> R.style.AppTheme_Lime_600_core
                -5983189 -> R.style.AppTheme_Lime_700_core
                -6382300 -> R.style.AppTheme_Lime_800_core
                -8227049 -> R.style.AppTheme_Lime_900_core

                -1596 -> R.style.AppTheme_Yellow_100_core
                -2672 -> R.style.AppTheme_Yellow_200_core
                -3722 -> R.style.AppTheme_Yellow_300_core
                -4520 -> R.style.AppTheme_Yellow_400_core
                -5317 -> R.style.AppTheme_Yellow_500_core
                -141259 -> R.style.AppTheme_Yellow_600_core
                -278483 -> R.style.AppTheme_Yellow_700_core
                -415707 -> R.style.AppTheme_Yellow_800_core
                -688361 -> R.style.AppTheme_Yellow_900_core

                -4941 -> R.style.AppTheme_Amber_100_core
                -8062 -> R.style.AppTheme_Amber_200_core
                -10929 -> R.style.AppTheme_Amber_300_core
                -13784 -> R.style.AppTheme_Amber_400_core
                -16121 -> R.style.AppTheme_Amber_500_core
                -19712 -> R.style.AppTheme_Amber_600_core
                -24576 -> R.style.AppTheme_Amber_700_core
                -28928 -> R.style.AppTheme_Amber_800_core
                -37120 -> R.style.AppTheme_Amber_900_core

                -8014 -> R.style.AppTheme_Orange_100_core
                -13184 -> R.style.AppTheme_Orange_200_core
                -18611 -> R.style.AppTheme_Orange_300_core
                -22746 -> R.style.AppTheme_Orange_400_core
                -26624 -> R.style.AppTheme_Orange_500_core
                -291840 -> R.style.AppTheme_Orange_600_core
                -689152 -> R.style.AppTheme_Orange_700_core
                -1086464 -> R.style.AppTheme_Orange_800_core
                -1683200 -> R.style.AppTheme_Orange_900_core

                -13124 -> R.style.AppTheme_Deep_Orange_100_core
                -21615 -> R.style.AppTheme_Deep_Orange_200_core
                -30107 -> R.style.AppTheme_Deep_Orange_300_core
                -36797 -> R.style.AppTheme_Deep_Orange_400_core
                -43230 -> R.style.AppTheme_Deep_Orange_500_core
                -765666 -> R.style.AppTheme_Deep_Orange_600_core
                -1684967 -> R.style.AppTheme_Deep_Orange_700_core
                -2604267 -> R.style.AppTheme_Deep_Orange_800_core
                -4246004 -> R.style.AppTheme_Deep_Orange_900_core

                -2634552 -> R.style.AppTheme_Brown_100_core
                -4412764 -> R.style.AppTheme_Brown_200_core
                -6190977 -> R.style.AppTheme_Brown_300_core
                -7508381 -> R.style.AppTheme_Brown_400_core
                -8825528 -> R.style.AppTheme_Brown_500_core
                -9614271 -> R.style.AppTheme_Brown_600_core
                -10665929 -> R.style.AppTheme_Brown_700_core
                -11652050 -> R.style.AppTheme_Brown_800_core
                -12703965 -> R.style.AppTheme_Brown_900_core

                -3155748 -> R.style.AppTheme_Blue_Grey_100_core
                -5194811 -> R.style.AppTheme_Blue_Grey_200_core
                -7297874 -> R.style.AppTheme_Blue_Grey_300_core
                -8875876 -> R.style.AppTheme_Blue_Grey_400_core
                -10453621 -> R.style.AppTheme_Blue_Grey_500_core
                -11243910 -> R.style.AppTheme_Blue_Grey_600_core
                -12232092 -> R.style.AppTheme_Blue_Grey_700_core
                -13154481 -> R.style.AppTheme_Blue_Grey_800_core
                -14273992 -> R.style.AppTheme_Blue_Grey_900_core

                -1 -> R.style.AppTheme_Grey_100_core
                -1118482 -> R.style.AppTheme_Grey_200_core
                -2039584 -> R.style.AppTheme_Grey_300_core
                -4342339 -> R.style.AppTheme_Grey_400_core
                -6381922 -> R.style.AppTheme_Grey_500_core
                -9079435 -> R.style.AppTheme_Grey_600_core
                -10395295 -> R.style.AppTheme_Grey_700_core
                -12434878 -> R.style.AppTheme_Grey_800_core
                -16777216 -> R.style.AppTheme_Grey_900_core

                else -> R.style.AppTheme_Orange_700_core
            }
        }

        else -> {
            when (color) {
                -12846 -> R.style.AppTheme_Red_100
                -1074534 -> R.style.AppTheme_Red_200
                -1739917 -> R.style.AppTheme_Red_300
                -1092784 -> R.style.AppTheme_Red_400
                -769226 -> R.style.AppTheme_Red_500
                -1754827 -> R.style.AppTheme_Red_600
                -2937041 -> R.style.AppTheme_Red_700
                -3790808 -> R.style.AppTheme_Red_800
                -4776932 -> R.style.AppTheme_Red_900

                -476208 -> R.style.AppTheme_Pink_100
                -749647 -> R.style.AppTheme_Pink_200
                -1023342 -> R.style.AppTheme_Pink_300
                -1294214 -> R.style.AppTheme_Pink_400
                -1499549 -> R.style.AppTheme_Pink_500
                -2614432 -> R.style.AppTheme_Pink_600
                -4056997 -> R.style.AppTheme_Pink_700
                -5434281 -> R.style.AppTheme_Pink_800
                -7860657 -> R.style.AppTheme_Pink_900

                -1982745 -> R.style.AppTheme_Purple_100
                -3238952 -> R.style.AppTheme_Purple_200
                -4560696 -> R.style.AppTheme_Purple_300
                -5552196 -> R.style.AppTheme_Purple_400
                -6543440 -> R.style.AppTheme_Purple_500
                -7461718 -> R.style.AppTheme_Purple_600
                -8708190 -> R.style.AppTheme_Purple_700
                -9823334 -> R.style.AppTheme_Purple_800
                -11922292 -> R.style.AppTheme_Purple_900

                -3029783 -> R.style.AppTheme_Deep_Purple_100
                -5005861 -> R.style.AppTheme_Deep_Purple_200
                -6982195 -> R.style.AppTheme_Deep_Purple_300
                -8497214 -> R.style.AppTheme_Deep_Purple_400
                -10011977 -> R.style.AppTheme_Deep_Purple_500
                -10603087 -> R.style.AppTheme_Deep_Purple_600
                -11457112 -> R.style.AppTheme_Deep_Purple_700
                -12245088 -> R.style.AppTheme_Deep_Purple_800
                -13558894 -> R.style.AppTheme_Deep_Purple_900

                -3814679 -> R.style.AppTheme_Indigo_100
                -6313766 -> R.style.AppTheme_Indigo_200
                -8812853 -> R.style.AppTheme_Indigo_300
                -10720320 -> R.style.AppTheme_Indigo_400
                -12627531 -> R.style.AppTheme_Indigo_500
                -13022805 -> R.style.AppTheme_Indigo_600
                -13615201 -> R.style.AppTheme_Indigo_700
                -14142061 -> R.style.AppTheme_Indigo_800
                -15064194 -> R.style.AppTheme_Indigo_900

                -4464901 -> R.style.AppTheme_Blue_100
                -7288071 -> R.style.AppTheme_Blue_200
                -10177034 -> R.style.AppTheme_Blue_300
                -12409355 -> R.style.AppTheme_Blue_400
                -14575885 -> R.style.AppTheme_Blue_500
                -14776091 -> R.style.AppTheme_Blue_600
                -15108398 -> R.style.AppTheme_Blue_700
                -15374912 -> R.style.AppTheme_Blue_800
                -15906911 -> R.style.AppTheme_Blue_900

                -4987396 -> R.style.AppTheme_Light_Blue_100
                -8268550 -> R.style.AppTheme_Light_Blue_200
                -11549705 -> R.style.AppTheme_Light_Blue_300
                -14043396 -> R.style.AppTheme_Light_Blue_400
                -16537100 -> R.style.AppTheme_Light_Blue_500
                -16540699 -> R.style.AppTheme_Light_Blue_600
                -16611119 -> R.style.AppTheme_Light_Blue_700
                -16615491 -> R.style.AppTheme_Light_Blue_800
                -16689253 -> R.style.AppTheme_Light_Blue_900

                -5051406 -> R.style.AppTheme_Cyan_100
                -8331542 -> R.style.AppTheme_Cyan_200
                -11677471 -> R.style.AppTheme_Cyan_300
                -14235942 -> R.style.AppTheme_Cyan_400
                -16728876 -> R.style.AppTheme_Cyan_500
                -16732991 -> R.style.AppTheme_Cyan_600
                -16738393 -> R.style.AppTheme_Cyan_700
                -16743537 -> R.style.AppTheme_Cyan_800
                -16752540 -> R.style.AppTheme_Cyan_900

                -5054501 -> R.style.AppTheme_Teal_100
                -8336444 -> R.style.AppTheme_Teal_200
                -11684180 -> R.style.AppTheme_Teal_300
                -14244198 -> R.style.AppTheme_Teal_400
                -16738680 -> R.style.AppTheme_Teal_500
                -16742021 -> R.style.AppTheme_Teal_600
                -16746133 -> R.style.AppTheme_Teal_700
                -16750244 -> R.style.AppTheme_Teal_800
                -16757440 -> R.style.AppTheme_Teal_900

                -3610935 -> R.style.AppTheme_Green_100
                -5908825 -> R.style.AppTheme_Green_200
                -8271996 -> R.style.AppTheme_Green_300
                -10044566 -> R.style.AppTheme_Green_400
                -11751600 -> R.style.AppTheme_Green_500
                -12345273 -> R.style.AppTheme_Green_600
                -13070788 -> R.style.AppTheme_Green_700
                -13730510 -> R.style.AppTheme_Green_800
                -14983648 -> R.style.AppTheme_Green_900

                -2298424 -> R.style.AppTheme_Light_Green_100
                -3808859 -> R.style.AppTheme_Light_Green_200
                -5319295 -> R.style.AppTheme_Light_Green_300
                -6501275 -> R.style.AppTheme_Light_Green_400
                -7617718 -> R.style.AppTheme_Light_Green_500
                -8604862 -> R.style.AppTheme_Light_Green_600
                -9920712 -> R.style.AppTheme_Light_Green_700
                -11171025 -> R.style.AppTheme_Light_Green_800
                -13407970 -> R.style.AppTheme_Light_Green_900

                -985917 -> R.style.AppTheme_Lime_100
                -1642852 -> R.style.AppTheme_Lime_200
                -2300043 -> R.style.AppTheme_Lime_300
                -2825897 -> R.style.AppTheme_Lime_400
                -3285959 -> R.style.AppTheme_Lime_500
                -4142541 -> R.style.AppTheme_Lime_600
                -5983189 -> R.style.AppTheme_Lime_700
                -6382300 -> R.style.AppTheme_Lime_800
                -8227049 -> R.style.AppTheme_Lime_900

                -1596 -> R.style.AppTheme_Yellow_100
                -2672 -> R.style.AppTheme_Yellow_200
                -3722 -> R.style.AppTheme_Yellow_300
                -4520 -> R.style.AppTheme_Yellow_400
                -5317 -> R.style.AppTheme_Yellow_500
                -141259 -> R.style.AppTheme_Yellow_600
                -278483 -> R.style.AppTheme_Yellow_700
                -415707 -> R.style.AppTheme_Yellow_800
                -688361 -> R.style.AppTheme_Yellow_900

                -4941 -> R.style.AppTheme_Amber_100
                -8062 -> R.style.AppTheme_Amber_200
                -10929 -> R.style.AppTheme_Amber_300
                -13784 -> R.style.AppTheme_Amber_400
                -16121 -> R.style.AppTheme_Amber_500
                -19712 -> R.style.AppTheme_Amber_600
                -24576 -> R.style.AppTheme_Amber_700
                -28928 -> R.style.AppTheme_Amber_800
                -37120 -> R.style.AppTheme_Amber_900

                -8014 -> R.style.AppTheme_Orange_100
                -13184 -> R.style.AppTheme_Orange_200
                -18611 -> R.style.AppTheme_Orange_300
                -22746 -> R.style.AppTheme_Orange_400
                -26624 -> R.style.AppTheme_Orange_500
                -291840 -> R.style.AppTheme_Orange_600
                -689152 -> R.style.AppTheme_Orange_700
                -1086464 -> R.style.AppTheme_Orange_800
                -1683200 -> R.style.AppTheme_Orange_900

                -13124 -> R.style.AppTheme_Deep_Orange_100
                -21615 -> R.style.AppTheme_Deep_Orange_200
                -30107 -> R.style.AppTheme_Deep_Orange_300
                -36797 -> R.style.AppTheme_Deep_Orange_400
                -43230 -> R.style.AppTheme_Deep_Orange_500
                -765666 -> R.style.AppTheme_Deep_Orange_600
                -1684967 -> R.style.AppTheme_Deep_Orange_700
                -2604267 -> R.style.AppTheme_Deep_Orange_800
                -4246004 -> R.style.AppTheme_Deep_Orange_900

                -2634552 -> R.style.AppTheme_Brown_100
                -4412764 -> R.style.AppTheme_Brown_200
                -6190977 -> R.style.AppTheme_Brown_300
                -7508381 -> R.style.AppTheme_Brown_400
                -8825528 -> R.style.AppTheme_Brown_500
                -9614271 -> R.style.AppTheme_Brown_600
                -10665929 -> R.style.AppTheme_Brown_700
                -11652050 -> R.style.AppTheme_Brown_800
                -12703965 -> R.style.AppTheme_Brown_900

                -3155748 -> R.style.AppTheme_Blue_Grey_100
                -5194811 -> R.style.AppTheme_Blue_Grey_200
                -7297874 -> R.style.AppTheme_Blue_Grey_300
                -8875876 -> R.style.AppTheme_Blue_Grey_400
                -10453621 -> R.style.AppTheme_Blue_Grey_500
                -11243910 -> R.style.AppTheme_Blue_Grey_600
                -12232092 -> R.style.AppTheme_Blue_Grey_700
                -13154481 -> R.style.AppTheme_Blue_Grey_800
                -14273992 -> R.style.AppTheme_Blue_Grey_900

                -1 -> R.style.AppTheme_Grey_100
                -1118482 -> R.style.AppTheme_Grey_200
                -2039584 -> R.style.AppTheme_Grey_300
                -4342339 -> R.style.AppTheme_Grey_400
                -6381922 -> R.style.AppTheme_Grey_500
                -9079435 -> R.style.AppTheme_Grey_600
                -10395295 -> R.style.AppTheme_Grey_700
                -12434878 -> R.style.AppTheme_Grey_800
                -16777216 -> R.style.AppTheme_Grey_900

                else -> R.style.AppTheme_Orange_700
            }
        }
    }

fun Activity.showBiometricPrompt(
    successCallback: ((String, Int) -> Unit)? = null,
    failureCallback: (() -> Unit)? = null
) {
    Class2BiometricAuthPrompt.Builder(getText(R.string.authenticate), getText(R.string.cancel))
        .build()
        .startAuthentication(
            AuthPromptHost(this as FragmentActivity),
            object : AuthPromptCallback() {
                override fun onAuthenticationSucceeded(activity: FragmentActivity?, result: BiometricPrompt.AuthenticationResult) {
                    successCallback?.invoke("", PROTECTION_FINGERPRINT)
                }

                override fun onAuthenticationError(activity: FragmentActivity?, errorCode: Int, errString: CharSequence) {
                    val isCanceledByUser = errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON || errorCode == BiometricPrompt.ERROR_USER_CANCELED
                    if (!isCanceledByUser) {
                        toast(errString.toString())
                    }
                    failureCallback?.invoke()
                }

                override fun onAuthenticationFailed(activity: FragmentActivity?) {
                    toast(R.string.authentication_failed)
                    failureCallback?.invoke()
                }
            }
        )
}