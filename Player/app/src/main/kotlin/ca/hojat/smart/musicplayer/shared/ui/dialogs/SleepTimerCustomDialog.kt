package ca.hojat.smart.musicplayer.shared.ui.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogCustomSleepTimerPickerBinding
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.hideKeyboard
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.showKeyboard
import ca.hojat.smart.musicplayer.shared.extensions.value
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding

class SleepTimerCustomDialog(val activity: Activity, val callback: (seconds: Int) -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogCustomSleepTimerPickerBinding::inflate)

    init {
        binding.minutesHint.hint =
            activity.getString(R.string.minutes_raw).replaceFirstChar { it.uppercaseChar() }
        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { _, _ -> dialogConfirmed() }
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.sleep_timer) { alertDialog ->
                    dialog = alertDialog
                    alertDialog.showKeyboard(binding.minutes)
                }
            }
    }

    private fun dialogConfirmed() {
        val value = binding.minutes.value
        val minutes = Integer.valueOf(value.ifEmpty { "0" })
        callback(minutes * 60)
        activity.hideKeyboard()
        dialog?.dismiss()
    }
}
