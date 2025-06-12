package ca.hojat.smart.musicplayer.shared.ui.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogNewPlaylistBinding
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.getPlaylistIdWithTitle
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.showKeyboard
import ca.hojat.smart.musicplayer.shared.extensions.value
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.data.models.Playlist
import ca.hojat.smart.musicplayer.shared.usecases.ShowToastUseCase

class PlaylistDialog(
    val activity: Activity,
    var playlist: Playlist? = null,
    val callback: (playlistId: Int) -> Unit
) {
    private var isNewPlaylist = playlist == null
    private val binding by activity.viewBinding(DialogNewPlaylistBinding::inflate)

    init {
        if (playlist == null) {
            playlist = Playlist(0, "")
        }

        binding.newPlaylistTitle.setText(playlist!!.title)
        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                val dialogTitle =
                    if (isNewPlaylist) R.string.create_new_playlist else R.string.rename_playlist
                activity.setupDialogStuff(binding.root, this, dialogTitle) { alertDialog ->
                    alertDialog.showKeyboard(binding.newPlaylistTitle)
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val title = binding.newPlaylistTitle.value
                        ensureBackgroundThread {
                            val playlistIdWithTitle = activity.getPlaylistIdWithTitle(title)
                            var isPlaylistTitleTaken = isNewPlaylist && playlistIdWithTitle != -1
                            if (!isPlaylistTitleTaken) {
                                isPlaylistTitleTaken =
                                    !isNewPlaylist && playlist!!.id != playlistIdWithTitle && playlistIdWithTitle != -1
                            }

                            if (title.isEmpty()) {
                                ShowToastUseCase(activity, R.string.empty_name)
                                return@ensureBackgroundThread
                            } else if (isPlaylistTitleTaken) {
                                ShowToastUseCase(activity, R.string.playlist_name_exists)
                                return@ensureBackgroundThread
                            }

                            playlist!!.title = title

                            val eventTypeId = if (isNewPlaylist) {
                                activity.audioHelper.insertPlaylist(playlist!!).toInt()
                            } else {
                                activity.audioHelper.updatePlaylist(playlist!!)
                                playlist!!.id
                            }

                            if (eventTypeId != -1) {
                                alertDialog.dismiss()
                                callback(eventTypeId)
                            } else {
                                ShowToastUseCase(activity, R.string.unknown_error_occurred)
                            }
                        }
                    }
                }
            }
    }
}
