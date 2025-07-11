package ca.hojat.smart.musicplayer.shared.ui.dialogs

import android.app.Activity
import android.view.ViewGroup
import android.widget.RadioGroup
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogChangeSortingBinding
import ca.hojat.smart.musicplayer.databinding.SmallRadioButtonBinding
import ca.hojat.smart.musicplayer.shared.extensions.beVisibleIf
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.helpers.ACTIVITY_PLAYLIST_FOLDER
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_ALBUM_COUNT
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_ARTIST_TITLE
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_CUSTOM
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_DATE_ADDED
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_DURATION
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_TITLE
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_TRACK_COUNT
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_TRACK_ID
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_YEAR
import ca.hojat.smart.musicplayer.shared.helpers.SORT_DESCENDING
import ca.hojat.smart.musicplayer.shared.helpers.TAB_ALBUMS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_ARTISTS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_FOLDERS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_GENRES
import ca.hojat.smart.musicplayer.shared.helpers.TAB_PLAYLISTS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_TRACKS
import ca.hojat.smart.musicplayer.shared.data.models.Playlist
import ca.hojat.smart.musicplayer.shared.data.models.RadioItem

class ChangeSortingDialog(
    val activity: Activity,
    private val location: Int,
    val playlist: Playlist? = null,
    val path: String? = null,
    val callback: () -> Unit
) {
    private val config = activity.config
    private var currSorting = 0
    private val binding by activity.viewBinding(DialogChangeSortingBinding::inflate)

    init {
        binding.apply {
            useForThisPlaylistDivider.root.beVisibleIf(playlist != null || path != null)
            sortingDialogUseForThisOnly.beVisibleIf(playlist != null || path != null)

            if (playlist != null) {
                sortingDialogUseForThisOnly.isChecked = config.hasCustomPlaylistSorting(playlist.id)
            } else if (path != null) {
                sortingDialogUseForThisOnly.text = activity.getString(R.string.use_for_this_folder)
                sortingDialogUseForThisOnly.isChecked = config.hasCustomSorting(path)
            }
        }

        currSorting = when (location) {
            TAB_PLAYLISTS -> config.playlistSorting
            TAB_FOLDERS -> config.folderSorting
            TAB_ARTISTS -> config.artistSorting
            TAB_ALBUMS -> config.albumSorting
            TAB_TRACKS -> config.trackSorting
            TAB_GENRES -> config.genreSorting
            else -> if (playlist != null) {
                config.getProperPlaylistSorting(playlist.id)
            } else if (path != null) {
                config.getProperFolderSorting(path)
            } else {
                config.trackSorting
            }
        }

        setupSortRadio()
        setupOrderRadio()

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { _, _ -> dialogConfirmed() }
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.sort_by)
            }
    }

    private fun setupSortRadio() {
        val radioItems = ArrayList<RadioItem>()
        when (location) {
            TAB_PLAYLISTS, TAB_FOLDERS -> {
                radioItems.add(
                    RadioItem(
                        0,
                        activity.getString(R.string.title),
                        PLAYER_SORT_BY_TITLE
                    )
                )
                radioItems.add(
                    RadioItem(
                        1,
                        activity.getString(R.string.track_count),
                        PLAYER_SORT_BY_TRACK_COUNT
                    )
                )
            }

            TAB_ARTISTS -> {
                radioItems.add(
                    RadioItem(
                        0,
                        activity.getString(R.string.title),
                        PLAYER_SORT_BY_TITLE
                    )
                )
                radioItems.add(
                    RadioItem(
                        1,
                        activity.getString(R.string.album_count),
                        PLAYER_SORT_BY_ALBUM_COUNT
                    )
                )
                radioItems.add(
                    RadioItem(
                        2,
                        activity.getString(R.string.track_count),
                        PLAYER_SORT_BY_TRACK_COUNT
                    )
                )
            }

            TAB_ALBUMS -> {
                radioItems.add(
                    RadioItem(
                        0,
                        activity.getString(R.string.title),
                        PLAYER_SORT_BY_TITLE
                    )
                )
                radioItems.add(
                    RadioItem(
                        1,
                        activity.getString(R.string.artist_name),
                        PLAYER_SORT_BY_ARTIST_TITLE
                    )
                )
                radioItems.add(RadioItem(2, activity.getString(R.string.year), PLAYER_SORT_BY_YEAR))
                radioItems.add(
                    RadioItem(
                        4,
                        activity.getString(R.string.date_added),
                        PLAYER_SORT_BY_DATE_ADDED
                    )
                )
            }

            TAB_TRACKS -> {
                radioItems.add(
                    RadioItem(
                        0,
                        activity.getString(R.string.title),
                        PLAYER_SORT_BY_TITLE
                    )
                )
                radioItems.add(
                    RadioItem(
                        1,
                        activity.getString(R.string.artist),
                        PLAYER_SORT_BY_ARTIST_TITLE
                    )
                )
                radioItems.add(
                    RadioItem(
                        2,
                        activity.getString(R.string.duration),
                        PLAYER_SORT_BY_DURATION
                    )
                )
                radioItems.add(
                    RadioItem(
                        3,
                        activity.getString(R.string.track_number),
                        PLAYER_SORT_BY_TRACK_ID
                    )
                )
                radioItems.add(
                    RadioItem(
                        4,
                        activity.getString(R.string.date_added),
                        PLAYER_SORT_BY_DATE_ADDED
                    )
                )
            }

            TAB_GENRES -> {
                radioItems.add(
                    RadioItem(
                        0,
                        activity.getString(R.string.title),
                        PLAYER_SORT_BY_TITLE
                    )
                )
                radioItems.add(
                    RadioItem(
                        2,
                        activity.getString(R.string.track_count),
                        PLAYER_SORT_BY_TRACK_COUNT
                    )
                )
            }

            ACTIVITY_PLAYLIST_FOLDER -> {
                radioItems.add(
                    RadioItem(
                        0,
                        activity.getString(R.string.title),
                        PLAYER_SORT_BY_TITLE
                    )
                )
                radioItems.add(
                    RadioItem(
                        1,
                        activity.getString(R.string.artist),
                        PLAYER_SORT_BY_ARTIST_TITLE
                    )
                )
                radioItems.add(
                    RadioItem(
                        2,
                        activity.getString(R.string.duration),
                        PLAYER_SORT_BY_DURATION
                    )
                )
                radioItems.add(
                    RadioItem(
                        3,
                        activity.getString(R.string.track_number),
                        PLAYER_SORT_BY_TRACK_ID
                    )
                )
                radioItems.add(
                    RadioItem(
                        4,
                        activity.getString(R.string.date_added),
                        PLAYER_SORT_BY_DATE_ADDED
                    )
                )

                if (playlist != null) {
                    radioItems.add(
                        RadioItem(
                            4,
                            activity.getString(R.string.custom),
                            PLAYER_SORT_BY_CUSTOM
                        )
                    )
                }
            }
        }

        binding.sortingDialogRadioSorting.setOnCheckedChangeListener { _, checkedId ->
            binding.sortingOrderDivider.beVisibleIf(checkedId != PLAYER_SORT_BY_CUSTOM)
            binding.sortingDialogRadioOrder.beVisibleIf(checkedId != PLAYER_SORT_BY_CUSTOM)
        }

        radioItems.forEach { radioItem ->
            SmallRadioButtonBinding.inflate(activity.layoutInflater).apply {
                smallRadioButton.apply {
                    text = radioItem.title
                    isChecked = currSorting and (radioItem.value as Int) != 0
                    id = radioItem.value
                }

                binding.sortingDialogRadioSorting.addView(
                    smallRadioButton,
                    RadioGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        }
    }

    private fun setupOrderRadio() {
        var orderBtn = binding.sortingDialogRadioAscending

        if (currSorting and SORT_DESCENDING != 0) {
            orderBtn = binding.sortingDialogRadioDescending
        }

        orderBtn.isChecked = true
    }

    private fun dialogConfirmed() {
        val sortingRadio = binding.sortingDialogRadioSorting
        var sorting = sortingRadio.checkedRadioButtonId

        if (binding.sortingDialogRadioOrder.checkedRadioButtonId == R.id.sorting_dialog_radio_descending) {
            sorting = sorting or SORT_DESCENDING
        }

        if (currSorting != sorting || location == ACTIVITY_PLAYLIST_FOLDER) {
            when (location) {
                TAB_PLAYLISTS -> config.playlistSorting = sorting
                TAB_FOLDERS -> config.folderSorting = sorting
                TAB_ARTISTS -> config.artistSorting = sorting
                TAB_ALBUMS -> config.albumSorting = sorting
                TAB_TRACKS -> config.trackSorting = sorting
                TAB_GENRES -> config.genreSorting = sorting
                ACTIVITY_PLAYLIST_FOLDER -> {
                    if (binding.sortingDialogUseForThisOnly.isChecked) {
                        if (playlist != null) {
                            config.saveCustomPlaylistSorting(playlist.id, sorting)
                        } else if (path != null) {
                            config.saveCustomSorting(path, sorting)
                        }
                    } else {
                        if (playlist != null) {
                            config.removeCustomPlaylistSorting(playlist.id)
                            config.playlistTracksSorting = sorting
                        } else if (path != null) {
                            config.removeCustomSorting(path)
                            config.playlistTracksSorting = sorting
                        } else {
                            config.trackSorting = sorting
                        }
                    }
                }
            }

            callback()
        }
    }
}
