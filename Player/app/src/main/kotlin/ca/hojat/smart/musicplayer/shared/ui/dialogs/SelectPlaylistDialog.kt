package ca.hojat.smart.musicplayer.shared.ui.dialogs

import android.app.Activity
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.databinding.DialogSelectPlaylistBinding
import ca.hojat.smart.musicplayer.databinding.ItemSelectPlaylistBinding
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.data.models.Playlist

class SelectPlaylistDialog(val activity: Activity, val callback: (playlistId: Int) -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogSelectPlaylistBinding::inflate)

    init {
        ensureBackgroundThread {
            val playlists = activity.audioHelper.getAllPlaylists()
            activity.runOnUiThread {
                initDialog(playlists)

                if (playlists.isEmpty()) {
                    showNewPlaylistDialog()
                }
            }
        }

        binding.dialogSelectPlaylistNewRadio.setOnClickListener {
            binding.dialogSelectPlaylistNewRadio.isChecked = false
            showNewPlaylistDialog()
        }
    }

    private fun initDialog(playlists: ArrayList<Playlist>) {
        playlists.forEach {
            ItemSelectPlaylistBinding.inflate(activity.layoutInflater).apply {
                val playlist = it
                selectPlaylistItemRadioButton.apply {
                    text = playlist.title
                    isChecked = false
                    id = playlist.id

                    setOnClickListener {
                        callback(playlist.id)
                        dialog?.dismiss()
                    }
                }

                binding.dialogSelectPlaylistLinear.addView(
                    this.root,
                    RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                )
            }
        }

        activity.getAlertDialogBuilder().apply {
            activity.setupDialogStuff(binding.root, this) { alertDialog ->
                dialog = alertDialog
            }
        }
    }

    private fun showNewPlaylistDialog() {
        PlaylistDialog(activity) {
            callback(it)
            dialog?.dismiss()
        }
    }
}
