package ca.hojat.notes.niki.shared.dialogs

import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.databinding.DialogRenameNoteBinding
import ca.hojat.notes.niki.shared.extensions.config
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.isAValidFilename
import ca.hojat.notes.niki.shared.extensions.notesDB
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff
import ca.hojat.notes.niki.shared.extensions.showKeyboard
import ca.hojat.notes.niki.shared.extensions.toast
import ca.hojat.notes.niki.shared.extensions.updateWidgets
import ca.hojat.notes.niki.shared.extensions.value
import ca.hojat.notes.niki.shared.helpers.NotesHelper
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.shared.data.models.Note
import java.io.File

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class RenameNoteDialog(
    val activity: BaseActivity,
    val note: Note,
    private val currentNoteText: String?,
    val callback: (note: Note) -> Unit
) {

    init {
        val binding = DialogRenameNoteBinding.inflate(activity.layoutInflater)
        val view = binding.root
        binding.lockedNoteTitle.setText(note.title)

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(view, this, R.string.rename_note) { alertDialog ->
                    alertDialog.showKeyboard(binding.lockedNoteTitle)
                    alertDialog.getButton(BUTTON_POSITIVE).setOnClickListener {
                        val title = binding.lockedNoteTitle.value
                        ensureBackgroundThread {
                            newTitleConfirmed(title, alertDialog)
                        }
                    }
                }
            }
    }


    private fun newTitleConfirmed(title: String, dialog: AlertDialog) {
        when {
            title.isEmpty() -> activity.toast(R.string.no_title)
            activity.notesDB.getNoteIdWithTitleCaseSensitive(title) != null -> activity.toast(R.string.title_taken)
            else -> {
                note.title = title
                if (activity.config.autosaveNotes && currentNoteText != null) {
                    note.value = currentNoteText
                }

                val path = note.path
                if (path.isEmpty()) {
                    activity.notesDB.insertOrUpdate(note)
                    activity.runOnUiThread {
                        dialog.dismiss()
                        callback(note)
                    }
                } else {
                    if (title.isEmpty()) {
                        activity.toast(R.string.filename_cannot_be_empty)
                        return
                    }

                    val file = File(path)
                    val newFile = File(file.parent, title)
                    if (!newFile.name.isAValidFilename()) {
                        activity.toast(R.string.invalid_name)
                        return
                    }

                    activity.renameFile(
                        file.absolutePath,
                        newFile.absolutePath,
                        false
                    ) { success, _ ->
                        if (success) {
                            note.path = newFile.absolutePath
                            NotesHelper(activity).insertOrUpdateNote(note) {
                                dialog.dismiss()
                                callback(note)
                            }
                        } else {
                            activity.toast(R.string.rename_file_error)
                            return@renameFile
                        }
                    }
                }

                activity.baseContext.updateWidgets()
            }
        }
    }
}
