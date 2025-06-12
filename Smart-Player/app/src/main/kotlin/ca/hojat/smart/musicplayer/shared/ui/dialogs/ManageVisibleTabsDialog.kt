package ca.hojat.smart.musicplayer.shared.ui.dialogs

import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogManageVisibleTabsBinding
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.helpers.TAB_ALBUMS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_ARTISTS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_FOLDERS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_GENRES
import ca.hojat.smart.musicplayer.shared.helpers.TAB_PLAYLISTS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_TRACKS
import ca.hojat.smart.musicplayer.shared.helpers.allTabsMask
import ca.hojat.smart.musicplayer.shared.ui.views.MyAppCompatCheckbox

class ManageVisibleTabsDialog(
    val activity: BaseSimpleActivity,
    val callback: (result: Int) -> Unit
) {
    private val binding by activity.viewBinding(DialogManageVisibleTabsBinding::inflate)
    private val tabs = LinkedHashMap<Int, MyAppCompatCheckbox>()

    init {
        tabs.apply {
            put(TAB_PLAYLISTS, binding.manageVisibleTabsPlaylists)
            put(TAB_FOLDERS, binding.manageVisibleTabsFolders)
            put(TAB_ARTISTS, binding.manageVisibleTabsArtists)
            put(TAB_ALBUMS, binding.manageVisibleTabsAlbums)
            put(TAB_TRACKS, binding.manageVisibleTabsTracks)
            put(TAB_GENRES, binding.manageVisibleTabsGenres)
        }

        val showTabs = activity.config.showTabs
        for ((key, value) in tabs) {
            value.isChecked = showTabs and key != 0
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { _, _ -> dialogConfirmed() }
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }

    private fun dialogConfirmed() {
        var result = 0
        for ((key, value) in tabs) {
            if (value.isChecked) {
                result += key
            }
        }

        if (result == 0) {
            result = allTabsMask
        }

        callback(result)
    }
}
