package com.simplemobiletools.smsmessenger.dialogs

import androidx.appcompat.app.AlertDialog
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.SimpleActivity
import com.simplemobiletools.smsmessenger.databinding.DialogExportMessagesBinding
import com.simplemobiletools.smsmessenger.extensions.config
import com.simplemobiletools.smsmessenger.extensions.getAlertDialogBuilder
import com.simplemobiletools.smsmessenger.extensions.getCurrentFormattedDateTime
import com.simplemobiletools.smsmessenger.extensions.isAValidFilename
import com.simplemobiletools.smsmessenger.extensions.setupDialogStuff
import com.simplemobiletools.smsmessenger.extensions.toast
import com.simplemobiletools.smsmessenger.extensions.value

class ExportMessagesDialog(
    private val activity: SimpleActivity,
    private val callback: (fileName: String) -> Unit,
) {
    private val config = activity.config

    init {
        val binding = DialogExportMessagesBinding.inflate(activity.layoutInflater).apply {
            exportSmsCheckbox.isChecked = config.exportSms
            exportMmsCheckbox.isChecked = config.exportMms
            exportMessagesFilename.setText(
                activity.getString(R.string.messages) + "_" + activity.getCurrentFormattedDateTime()
            )
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(
                    binding.root,
                    this,
                    R.string.export_messages
                ) { alertDialog ->
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        config.exportSms = binding.exportSmsCheckbox.isChecked
                        config.exportMms = binding.exportMmsCheckbox.isChecked
                        val filename = binding.exportMessagesFilename.value
                        when {
                            filename.isEmpty() -> activity.toast(R.string.empty_name)
                            filename.isAValidFilename() -> {
                                callback(filename)
                                alertDialog.dismiss()
                            }

                            else -> activity.toast(R.string.invalid_name)
                        }
                    }
                }
            }
    }
}
