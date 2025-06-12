package ca.hojat.notes.niki.shared.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.databinding.DialogImportFolderBinding
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.getFilenameFromPath
import ca.hojat.notes.niki.shared.extensions.humanizePath
import ca.hojat.notes.niki.shared.extensions.isMediaFile
import ca.hojat.notes.niki.shared.extensions.notesDB
import ca.hojat.notes.niki.shared.extensions.parseChecklistItems
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff
import ca.hojat.notes.niki.shared.helpers.NotesHelper
import ca.hojat.notes.niki.shared.helpers.PROTECTION_NONE
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.shared.data.models.Note
import ca.hojat.notes.niki.shared.data.models.NoteType
import java.io.File

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class ImportFolderDialog(
    val activity: BaseActivity,
    val path: String,
    val callback: () -> Unit
) :
    AlertDialog.Builder(activity) {
    private var dialog: AlertDialog? = null

    init {
        val binding = DialogImportFolderBinding.inflate(activity.layoutInflater).apply {
            openFileFilename.setText(activity.humanizePath(path))
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(
                    binding.root,
                    this,
                    R.string.import_folder
                ) { alertDialog ->
                    dialog = alertDialog
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val updateFilesOnEdit =
                            binding.openFileType.checkedRadioButtonId == R.id.open_file_update_file
                        ensureBackgroundThread {
                            saveFolder(updateFilesOnEdit)
                        }
                    }
                }
            }
    }


    private fun saveFolder(updateFilesOnEdit: Boolean) {
        val folder = File(path)
        folder.listFiles { file ->
            val filename = file.path.getFilenameFromPath()
            when {
                file.isDirectory -> false
                filename.isMediaFile() -> false
                file.length() > 1000 * 1000 -> false
                activity.notesDB.getNoteIdWithTitle(filename) != null -> false
                else -> true
            }
        }?.forEach {
            val storePath = if (updateFilesOnEdit) it.absolutePath else ""
            val title = it.absolutePath.getFilenameFromPath()
            val value = if (updateFilesOnEdit) "" else it.readText()
            val fileText = it.readText().trim()
            val checklistItems = fileText.parseChecklistItems()
            if (checklistItems != null) {
                saveNote(title.substringBeforeLast('.'), fileText, NoteType.TYPE_CHECKLIST, "")
            } else {
                if (updateFilesOnEdit) {
                    activity.handleSAFDialog(path) {
                        saveNote(title, value, NoteType.TYPE_TEXT, storePath)
                    }
                } else {
                    saveNote(title, value, NoteType.TYPE_TEXT, storePath)
                }
            }
        }

        activity.runOnUiThread {
            callback()
            dialog?.dismiss()
        }
    }

    private fun saveNote(title: String, value: String, type: NoteType, path: String) {
        val note = Note(null, title, value, type, path, PROTECTION_NONE, "")
        NotesHelper(activity).insertOrUpdateNote(note)
    }
}
