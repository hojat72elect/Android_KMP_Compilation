package com.simplemobiletools.smsmessenger.dialogs

import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.BaseSimpleActivity
import com.simplemobiletools.smsmessenger.extensions.getAlertDialogBuilder
import com.simplemobiletools.smsmessenger.extensions.setupDialogStuff
import com.simplemobiletools.smsmessenger.databinding.DialogSelectTextBinding

// helper dialog for selecting just a part of a message, not copying the whole into clipboard
class SelectTextDialog(val activity: BaseSimpleActivity, val text: String) {
    init {
        val binding = DialogSelectTextBinding.inflate(activity.layoutInflater).apply {
            dialogSelectTextValue.text = text
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { _, _ -> { } }
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }
}
