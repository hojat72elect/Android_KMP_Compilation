package ca.hojat.notes.niki.shared.dialogs

import android.app.Activity
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AlertDialog
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.databinding.DialogTextviewBinding
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.getStringsPackageName
import ca.hojat.notes.niki.shared.extensions.launchViewIntent
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff

class AppSideLoadedDialog(val activity: Activity, val callback: () -> Unit) {
    private var dialog: AlertDialog? = null
    private val url =
        "https://play.google.com/store/apps/details?id=${activity.getStringsPackageName()}"

    init {
        val view = DialogTextviewBinding.inflate(activity.layoutInflater, null, false).apply {
            val text = String.format(activity.getString(R.string.sideloaded_app), url)
            textView.text = Html.fromHtml(text)
            textView.movementMethod = LinkMovementMethod.getInstance()
        }

        activity.getAlertDialogBuilder()
            .setNegativeButton(R.string.cancel) { _, _ -> negativePressed() }
            .setPositiveButton(R.string.download, null)
            .setOnCancelListener { negativePressed() }
            .apply {
                activity.setupDialogStuff(view.root, this, R.string.app_corrupt) { alertDialog ->
                    dialog = alertDialog
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        downloadApp()
                    }
                }
            }
    }

    private fun downloadApp() {
        activity.launchViewIntent(url)
    }

    private fun negativePressed() {
        dialog?.dismiss()
        callback()
    }
}
