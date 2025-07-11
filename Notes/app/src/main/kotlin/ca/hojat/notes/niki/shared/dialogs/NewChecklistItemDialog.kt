package ca.hojat.notes.niki.shared.dialogs

import android.app.Activity
import android.content.DialogInterface.BUTTON_POSITIVE
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import ca.hojat.notes.niki.shared.helpers.DARK_GREY
import ca.hojat.notes.niki.shared.helpers.SORT_BY_CUSTOM
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.databinding.DialogNewChecklistItemBinding
import ca.hojat.notes.niki.databinding.ItemAddChecklistBinding
import ca.hojat.notes.niki.shared.extensions.applyColorFilter
import ca.hojat.notes.niki.shared.extensions.beVisibleIf
import ca.hojat.notes.niki.shared.extensions.config
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.getContrastColor
import ca.hojat.notes.niki.shared.extensions.getProperPrimaryColor
import ca.hojat.notes.niki.shared.extensions.isWhiteTheme
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff
import ca.hojat.notes.niki.shared.extensions.showKeyboard
import ca.hojat.notes.niki.shared.extensions.toast
import ca.hojat.notes.niki.shared.extensions.updateTextColors

class NewChecklistItemDialog(
    val activity: Activity,
    callback: (titles: ArrayList<String>) -> Unit
) {
    private val titles = mutableListOf<AppCompatEditText>()
    private val binding = DialogNewChecklistItemBinding.inflate(activity.layoutInflater)
    private val view = binding.root

    init {
        addNewEditText()
        val plusTextColor = if (activity.isWhiteTheme()) {
            DARK_GREY
        } else {
            activity.getProperPrimaryColor().getContrastColor()
        }

        binding.apply {
            addItem.applyColorFilter(plusTextColor)
            addItem.setOnClickListener {
                addNewEditText()
            }
            settingsAddChecklistTop.beVisibleIf(activity.config.sorting == SORT_BY_CUSTOM)
            settingsAddChecklistTop.isChecked = activity.config.addNewChecklistItemsTop
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(
                    view,
                    this,
                    R.string.add_new_checklist_items
                ) { alertDialog ->
                    alertDialog.showKeyboard(titles.first())
                    alertDialog.getButton(BUTTON_POSITIVE).setOnClickListener {
                        activity.config.addNewChecklistItemsTop =
                            binding.settingsAddChecklistTop.isChecked
                        when {
                            titles.all { it.text!!.isEmpty() } -> activity.toast(R.string.empty_name)
                            else -> {
                                val titles =
                                    titles.map { it.text.toString() }.filter { it.isNotEmpty() }
                                        .toMutableList() as ArrayList<String>
                                callback(titles)
                                alertDialog.dismiss()
                            }
                        }
                    }
                }
            }
    }

    private fun addNewEditText() {
        ItemAddChecklistBinding.inflate(activity.layoutInflater).apply {
            titleEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER) {
                    addNewEditText()
                    true
                } else {
                    false
                }
            }
            titles.add(titleEditText)
            binding.checklistHolder.addView(this.root)
            activity.updateTextColors(binding.checklistHolder)
            binding.dialogHolder.post {
                binding.dialogHolder.fullScroll(View.FOCUS_DOWN)
                activity.showKeyboard(titleEditText)
            }
        }
    }
}
