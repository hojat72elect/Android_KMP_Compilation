package ca.hojat.notes.niki.shared.dialogs

import android.app.Activity
import android.content.DialogInterface.BUTTON_POSITIVE
import ca.hojat.notes.niki.shared.helpers.PROTECTION_NONE
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.databinding.DialogNewNoteBinding
import ca.hojat.notes.niki.shared.extensions.config
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.notesDB
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff
import ca.hojat.notes.niki.shared.extensions.showKeyboard
import ca.hojat.notes.niki.shared.extensions.toast
import ca.hojat.notes.niki.shared.extensions.value
import ca.hojat.notes.niki.shared.data.models.Note
import ca.hojat.notes.niki.shared.data.models.NoteType

class NewNoteDialog(
    val activity: Activity,
    title: String? = null,
    val setChecklistAsDefault: Boolean,
    callback: (note: Note) -> Unit
) {
    init {
        val binding = DialogNewNoteBinding.inflate(activity.layoutInflater).apply {
            val defaultType = when {
                setChecklistAsDefault -> typeChecklist.id
                activity.config.lastCreatedNoteType == NoteType.TYPE_TEXT.value -> typeTextNote.id
                else -> typeChecklist.id
            }

            newNoteType.check(defaultType)
        }

        binding.lockedNoteTitle.setText(title)

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.new_note) { alertDialog ->
                    alertDialog.showKeyboard(binding.lockedNoteTitle)
                    alertDialog.getButton(BUTTON_POSITIVE).setOnClickListener {
                        val newTitle = binding.lockedNoteTitle.value
                        ensureBackgroundThread {
                            when {
                                newTitle.isEmpty() -> activity.toast(R.string.no_title)
                                activity.notesDB.getNoteIdWithTitle(newTitle) != null -> activity.toast(
                                    R.string.title_taken
                                )

                                else -> {
                                    val type =
                                        if (binding.newNoteType.checkedRadioButtonId == binding.typeChecklist.id) {
                                            NoteType.TYPE_CHECKLIST
                                        } else {
                                            NoteType.TYPE_TEXT
                                        }

                                    activity.config.lastCreatedNoteType = type.value
                                    val newNote =
                                        Note(null, newTitle, "", type, "", PROTECTION_NONE, "")
                                    callback(newNote)
                                    alertDialog.dismiss()
                                }
                            }
                        }
                    }
                }
            }
    }
}
