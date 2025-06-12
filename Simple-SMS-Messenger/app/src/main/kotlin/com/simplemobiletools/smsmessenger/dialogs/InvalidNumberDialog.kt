package com.simplemobiletools.smsmessenger.dialogs

import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.BaseSimpleActivity
import com.simplemobiletools.smsmessenger.extensions.getAlertDialogBuilder
import com.simplemobiletools.smsmessenger.extensions.setupDialogStuff
import com.simplemobiletools.smsmessenger.databinding.DialogInvalidNumberBinding

class InvalidNumberDialog(val activity: BaseSimpleActivity, val text: String) {
    init {
        val binding = DialogInvalidNumberBinding.inflate(activity.layoutInflater).apply {
            dialogInvalidNumberDesc.text = text
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { _, _ -> { } }
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }
}
