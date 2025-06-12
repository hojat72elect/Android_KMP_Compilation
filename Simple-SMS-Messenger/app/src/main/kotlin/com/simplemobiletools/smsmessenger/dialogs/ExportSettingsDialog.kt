package com.simplemobiletools.smsmessenger.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.databinding.DialogExportSettingsBinding
import com.simplemobiletools.smsmessenger.extensions.baseConfig
import com.simplemobiletools.smsmessenger.extensions.beGone
import com.simplemobiletools.smsmessenger.extensions.getAlertDialogBuilder
import com.simplemobiletools.smsmessenger.extensions.getDoesFilePathExist
import com.simplemobiletools.smsmessenger.extensions.getFilenameFromPath
import com.simplemobiletools.smsmessenger.extensions.isAValidFilename
import com.simplemobiletools.smsmessenger.extensions.setupDialogStuff
import com.simplemobiletools.smsmessenger.extensions.toast
import com.simplemobiletools.smsmessenger.extensions.value
import com.simplemobiletools.smsmessenger.activities.BaseSimpleActivity
import com.simplemobiletools.smsmessenger.extensions.humanizePath
import com.simplemobiletools.smsmessenger.extensions.internalStoragePath

@RequiresApi(Build.VERSION_CODES.O)
class ExportSettingsDialog(
    val activity: BaseSimpleActivity, val defaultFilename: String, val hidePath: Boolean,
    callback: (path: String, filename: String) -> Unit
) {
    init {
        val lastUsedFolder = activity.baseConfig.lastExportedSettingsFolder
        var folder =
            if (lastUsedFolder.isNotEmpty() && activity.getDoesFilePathExist(lastUsedFolder)) {
                lastUsedFolder
            } else {
                activity.internalStoragePath
            }

        val view = DialogExportSettingsBinding.inflate(activity.layoutInflater, null, false).apply {
            exportSettingsFilename.setText(defaultFilename.removeSuffix(".txt"))

            if (hidePath) {
                exportSettingsPathHint.beGone()
            } else {
                exportSettingsPath.setText(activity.humanizePath(folder))
                exportSettingsPath.setOnClickListener {
                    FilePickerDialog(activity, folder, false, showFAB = true) {
                        exportSettingsPath.setText(activity.humanizePath(it))
                        folder = it
                    }
                }
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(
                    view.root,
                    this,
                    R.string.export_settings
                ) { alertDialog ->
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        var filename = view.exportSettingsFilename.value
                        if (filename.isEmpty()) {
                            activity.toast(R.string.filename_cannot_be_empty)
                            return@setOnClickListener
                        }

                        filename += ".txt"
                        val newPath = "${folder.trimEnd('/')}/$filename"
                        if (!newPath.getFilenameFromPath().isAValidFilename()) {
                            activity.toast(R.string.filename_invalid_characters)
                            return@setOnClickListener
                        }

                        activity.baseConfig.lastExportedSettingsFolder = folder
                        if (!hidePath && activity.getDoesFilePathExist(newPath)) {
                            val title = String.format(
                                activity.getString(R.string.file_already_exists_overwrite),
                                newPath.getFilenameFromPath()
                            )
                            ConfirmationDialog(activity, title) {
                                callback(newPath, filename)
                                alertDialog.dismiss()
                            }
                        } else {
                            callback(newPath, filename)
                            alertDialog.dismiss()
                        }
                    }
                }
            }
    }
}
