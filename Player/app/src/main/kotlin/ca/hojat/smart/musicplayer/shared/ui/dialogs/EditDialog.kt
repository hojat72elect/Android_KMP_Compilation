package ca.hojat.smart.musicplayer.shared.ui.dialogs

import androidx.appcompat.app.AlertDialog
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogRenameSongBinding
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.data.models.Track
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.extensions.beGone
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.getFilenameExtension
import ca.hojat.smart.musicplayer.shared.extensions.getFilenameFromPath
import ca.hojat.smart.musicplayer.shared.extensions.getParentPath
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.showKeyboard
import ca.hojat.smart.musicplayer.shared.extensions.value
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.helpers.TagHelper
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.shared.usecases.ShowToastUseCase

class EditDialog(
    val activity: BaseSimpleActivity,
    val track: Track,
    val callback: (track: Track) -> Unit
) {
    private val tagHelper = TagHelper(activity)
    private val binding by activity.viewBinding(DialogRenameSongBinding::inflate)

    init {
        binding.apply {
            title.setText(track.title)
            artist.setText(track.artist)
            album.setText(track.album)
            val filename = track.path.getFilenameFromPath()
            fileName.setText(filename.substring(0, filename.lastIndexOf(".")))
            extension.setText(track.path.getFilenameExtension())
            arrayOf(fileNameHint, extensionHint).forEach {
                it.beGone()
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.rename_song) { alertDialog ->
                    alertDialog.showKeyboard(binding.title)
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val newTitle = binding.title.value
                        val newArtist = binding.artist.value
                        val newAlbum = binding.album.value
                        val newFilename = binding.fileName.value
                        val newFileExtension = binding.extension.value

                        if (newTitle.isEmpty() || newArtist.isEmpty() || newFilename.isEmpty() || newFileExtension.isEmpty()) {
                            ShowToastUseCase(activity, R.string.rename_song_empty)
                            return@setOnClickListener
                        }

                        if (track.title != newTitle || track.artist != newArtist || track.album != newAlbum) {
                            updateContentResolver(track, newArtist, newTitle, newAlbum) {
                                track.artist = newArtist
                                track.title = newTitle
                                track.album = newAlbum
                                val oldPath = track.path
                                val newPath =
                                    "${oldPath.getParentPath()}/$newFilename.$newFileExtension"
                                if (oldPath == newPath) {
                                    storeEditedSong(track, oldPath, newPath)
                                    callback(track)
                                    alertDialog.dismiss()
                                    return@updateContentResolver
                                }
                            }
                        } else {
                            alertDialog.dismiss()
                        }
                    }
                }
            }
    }

    private fun storeEditedSong(track: Track, oldPath: String, newPath: String) {
        ensureBackgroundThread {
            try {
                activity.audioHelper.updateTrackInfo(newPath, track.artist, track.title, oldPath)
            } catch (e: Exception) {
                ShowToastUseCase(activity, "The error : $e")
            }
        }
    }

    private fun updateContentResolver(
        track: Track,
        newArtist: String,
        newTitle: String,
        newAlbum: String,
        onUpdateMediaStore: () -> Unit
    ) {
        ensureBackgroundThread {
            try {
                activity.handleRecoverableSecurityException { granted ->
                    if (granted) {
                        tagHelper.writeTag(track, newArtist, newTitle, newAlbum)
                        activity.runOnUiThread {
                            onUpdateMediaStore.invoke()
                        }
                    }
                }
            } catch (e: Exception) {
                ShowToastUseCase(activity, R.string.unknown_error_occurred)
            }
        }
    }
}
