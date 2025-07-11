package ca.hojat.smart.musicplayer.shared.ui.dialogs

import android.app.Activity
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogRemovePlaylistBinding
import ca.hojat.smart.musicplayer.shared.data.models.Playlist

class RemovePlaylistDialog(val activity: Activity, val playlist: Playlist? = null, val callback: (deleteFiles: Boolean) -> Unit) {
    private val binding by activity.viewBinding(DialogRemovePlaylistBinding::inflate)

    init {
        binding.removePlaylistDescription.text = getDescriptionText()
        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { _, _ -> callback(binding.removePlaylistCheckbox.isChecked) }
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.remove_playlist)
            }
    }

    private fun getDescriptionText(): String {
        return if (playlist == null) {
            activity.getString(R.string.remove_playlist_description)
        } else
            String.format(activity.resources.getString(R.string.remove_playlist_description_placeholder), playlist.title)
    }
}
