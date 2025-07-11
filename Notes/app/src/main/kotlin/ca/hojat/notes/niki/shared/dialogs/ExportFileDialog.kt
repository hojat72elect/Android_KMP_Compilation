package ca.hojat.notes.niki.shared.dialogs

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.databinding.DialogExportFileBinding
import ca.hojat.notes.niki.shared.extensions.config
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.humanizePath
import ca.hojat.notes.niki.shared.extensions.isAValidFilename
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff
import ca.hojat.notes.niki.shared.extensions.showKeyboard
import ca.hojat.notes.niki.shared.extensions.toast
import ca.hojat.notes.niki.shared.extensions.value
import ca.hojat.notes.niki.shared.data.models.Note
import java.io.File

@SuppressLint("StringFormatMatches")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class ExportFileDialog(
    val activity: BaseActivity,
    val note: Note,
    val callback: (exportPath: String) -> Unit
) {

    init {
        var realPath = File(note.path).parent ?: activity.config.lastUsedSavePath
        val binding = DialogExportFileBinding.inflate(activity.layoutInflater).apply {
            filePath.setText(activity.humanizePath(realPath))

            fileName.setText(note.title)
            extension.setText(activity.config.lastUsedExtension)
            filePath.setOnClickListener {
                FilePickerDialog(
                    activity = activity,
                    currPath = realPath,
                    pickFile = false,
                    showHidden = false,
                    showFAB = true,
                    canAddShowHiddenButton = true
                ) {
                    filePath.setText(activity.humanizePath(it))
                    realPath = it
                }
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(
                    binding.root,
                    this,
                    R.string.export_as_file
                ) { alertDialog ->
                    alertDialog.showKeyboard(binding.fileName)
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val filename = binding.fileName.value
                        val extension = binding.extension.value

                        if (filename.isEmpty()) {
                            activity.toast(R.string.filename_cannot_be_empty)
                            return@setOnClickListener
                        }

                        val fullFilename =
                            if (extension.isEmpty()) filename else "$filename.$extension"
                        if (!fullFilename.isAValidFilename()) {
                            activity.toast(
                                String.format(
                                    activity.getString(
                                        R.string.filename_invalid_characters_placeholder,
                                        fullFilename
                                    )
                                )
                            )
                            return@setOnClickListener
                        }

                        activity.config.lastUsedExtension = extension
                        activity.config.lastUsedSavePath = realPath
                        callback("$realPath/$fullFilename")
                        alertDialog.dismiss()
                    }
                }
            }
    }
}
