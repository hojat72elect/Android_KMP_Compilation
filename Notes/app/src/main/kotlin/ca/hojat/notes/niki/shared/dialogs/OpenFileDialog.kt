package ca.hojat.notes.niki.shared.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.databinding.DialogOpenFileBinding
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.getFilenameFromPath
import ca.hojat.notes.niki.shared.extensions.humanizePath
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff
import ca.hojat.notes.niki.shared.helpers.PROTECTION_NONE
import ca.hojat.notes.niki.shared.data.models.Note
import ca.hojat.notes.niki.shared.data.models.NoteType
import java.io.File

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class OpenFileDialog(
    val activity: BaseActivity,
    val path: String,
    val callback: (note: Note) -> Unit
) : AlertDialog.Builder(activity) {
    private var dialog: AlertDialog? = null

    init {
        val binding = DialogOpenFileBinding.inflate(activity.layoutInflater).apply {
            openFileFilename.setText(activity.humanizePath(path))
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.open_file) { alertDialog ->
                    dialog = alertDialog
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val updateFileOnEdit =
                            binding.openFileType.checkedRadioButtonId == binding.openFileUpdateFile.id
                        val storePath = if (updateFileOnEdit) path else ""
                        val storeContent = if (updateFileOnEdit) "" else File(path).readText()

                        if (updateFileOnEdit) {
                            activity.handleSAFDialog(path) {
                                saveNote(storeContent, storePath)
                            }
                        } else {
                            saveNote(storeContent, storePath)
                        }
                    }
                }
            }
    }

    private fun saveNote(storeContent: String, storePath: String) {
        val filename = path.getFilenameFromPath()
        val note =
            Note(null, filename, storeContent, NoteType.TYPE_TEXT, storePath, PROTECTION_NONE, "")
        callback(note)
        dialog?.dismiss()
    }
}
