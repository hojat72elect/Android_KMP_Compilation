package ca.hojat.smart.musicplayer.shared.ui.dialogs

import androidx.appcompat.app.AlertDialog
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogExportPlaylistBinding
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.extensions.beGone
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.getCurrentFormattedDateTime
import ca.hojat.smart.musicplayer.shared.extensions.getParentPath
import ca.hojat.smart.musicplayer.shared.extensions.hideKeyboard
import ca.hojat.smart.musicplayer.shared.extensions.humanizePath
import ca.hojat.smart.musicplayer.shared.extensions.internalStoragePath
import ca.hojat.smart.musicplayer.shared.extensions.isAValidFilename
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.value
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.shared.ui.dialogs.filepicker.FilePickerDialog
import ca.hojat.smart.musicplayer.shared.usecases.ShowToastUseCase
import java.io.File

class ExportPlaylistDialog(
    val activity: BaseSimpleActivity,
    val path: String,
    private val hidePath: Boolean,
    private val callback: (file: File) -> Unit
) {
    private var ignoreClicks = false
    private var realPath = path.ifEmpty { activity.internalStoragePath }
    private val binding by activity.viewBinding(DialogExportPlaylistBinding::inflate)

    init {
        binding.apply {
            exportPlaylistFolder.text = activity.humanizePath(realPath)

            val fileName = "playlist_${getCurrentFormattedDateTime()}"
            exportPlaylistFilename.setText(fileName)

            if (hidePath) {
                exportPlaylistFolderLabel.beGone()
                exportPlaylistFolder.beGone()
            } else {
                exportPlaylistFolder.setOnClickListener {
                    activity.hideKeyboard(exportPlaylistFilename)
                    FilePickerDialog(activity, realPath, false, showFAB = true) {
                        exportPlaylistFolder.text = activity.humanizePath(it)
                        realPath = it
                    }
                }
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(
                    binding.root,
                    this,
                    R.string.export_playlist
                ) { alertDialog ->
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val filename = binding.exportPlaylistFilename.value
                        when {
                            filename.isEmpty() -> ShowToastUseCase(activity, R.string.empty_name)
                            filename.isAValidFilename() -> {
                                val file = File(realPath, "$filename.m3u")
                                if (!hidePath && file.exists()) {
                                    ShowToastUseCase(activity, R.string.name_taken)
                                    return@setOnClickListener
                                }

                                ignoreClicks = true
                                ensureBackgroundThread {
                                    activity.config.lastExportPath =
                                        file.absolutePath.getParentPath()
                                    callback(file)
                                    alertDialog.dismiss()
                                }
                            }

                            else -> ShowToastUseCase(activity, R.string.invalid_name)
                        }
                    }
                }
            }
    }
}
