package ca.hojat.notes.niki.shared.dialogs

import android.app.Activity
import android.view.LayoutInflater
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.databinding.DialogWhatsNewBinding
import ca.hojat.notes.niki.shared.data.models.Release
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff

class WhatsNewDialog(val activity: Activity, private val releases: List<Release>) {
    init {
        val view = DialogWhatsNewBinding.inflate(LayoutInflater.from(activity), null, false)
        view.whatsNewContent.text = getNewReleases()

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .apply {
                activity.setupDialogStuff(
                    view.root,
                    this,
                    R.string.whats_new,
                    cancelOnTouchOutside = false
                )
            }
    }

    private fun getNewReleases(): String {
        val sb = StringBuilder()

        releases.forEach {
            val parts = activity.getString(it.textId).split("\n").map(String::trim)
            parts.forEach { part ->
                sb.append("- $part\n")
            }
        }

        return sb.toString()
    }
}

