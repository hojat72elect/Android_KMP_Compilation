package ca.hojat.smart.musicplayer.feature_playlists

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import com.google.gson.Gson
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.extensions.areSystemAnimationsEnabled
import ca.hojat.smart.musicplayer.shared.extensions.beGoneIf
import ca.hojat.smart.musicplayer.shared.extensions.beVisibleIf
import ca.hojat.smart.musicplayer.shared.extensions.hideKeyboard
import ca.hojat.smart.musicplayer.shared.extensions.underlineText
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.feature_tracks.TracksActivity
import ca.hojat.smart.musicplayer.databinding.FragmentPlaylistsBinding
import ca.hojat.smart.musicplayer.shared.ui.dialogs.ChangeSortingDialog
import ca.hojat.smart.musicplayer.shared.ui.dialogs.PlaylistDialog
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.mediaScanner
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.MyViewPagerFragment
import ca.hojat.smart.musicplayer.shared.helpers.PLAYLIST
import ca.hojat.smart.musicplayer.shared.helpers.TAB_PLAYLISTS
import ca.hojat.smart.musicplayer.shared.data.models.Events
import ca.hojat.smart.musicplayer.shared.data.models.Playlist
import ca.hojat.smart.musicplayer.shared.data.models.sortSafely
import org.greenrobot.eventbus.EventBus

class PlaylistsFragment(context: Context, attributeSet: AttributeSet) :
    MyViewPagerFragment(context, attributeSet) {
    private var playlists = ArrayList<Playlist>()
    private val binding by viewBinding(FragmentPlaylistsBinding::bind)

    override fun setupFragment(activity: BaseSimpleActivity) {
        binding.playlistsPlaceholder2.underlineText()
        binding.playlistsPlaceholder2.setOnClickListener {
            PlaylistDialog(activity) {
                EventBus.getDefault().post(Events.PlaylistsUpdated())
            }
        }

        ensureBackgroundThread {
            val playlists = context.audioHelper.getAllPlaylists()
            playlists.forEach {
                it.trackCount = context.audioHelper.getPlaylistTrackCount(it.id)
            }

            playlists.sortSafely(context.config.playlistSorting)
            this.playlists = playlists

            activity.runOnUiThread {
                val scanning = activity.mediaScanner.isScanning()
                binding.playlistsPlaceholder.text = if (scanning) {
                    context.getString(R.string.loading_files)
                } else {
                    context.getString(R.string.no_items_found)
                }
                binding.playlistsPlaceholder.beVisibleIf(playlists.isEmpty())
                binding.playlistsPlaceholder2.beVisibleIf(playlists.isEmpty() && !scanning)

                val adapter = binding.playlistsList.adapter
                if (adapter == null) {
                    PlaylistsAdapter(activity, playlists, binding.playlistsList) {
                        activity.hideKeyboard()
                        Intent(activity, TracksActivity::class.java).apply {
                            putExtra(PLAYLIST, Gson().toJson(it))
                            activity.startActivity(this)
                        }
                    }.apply {
                        binding.playlistsList.adapter = this
                    }

                    if (context.areSystemAnimationsEnabled) {
                        binding.playlistsList.scheduleLayoutAnimation()
                    }
                } else {
                    (adapter as PlaylistsAdapter).updateItems(playlists)
                }
            }
        }
    }

    override fun finishActMode() {
        getAdapter()?.finishActMode()
    }

    override fun onSearchQueryChanged(text: String) {
        val filtered = playlists.filter { it.title.contains(text, true) }
            .toMutableList() as ArrayList<Playlist>
        getAdapter()?.updateItems(filtered, text)
        binding.playlistsPlaceholder.beVisibleIf(filtered.isEmpty())
        binding.playlistsPlaceholder2.beVisibleIf(filtered.isEmpty())
    }

    override fun onSearchClosed() {
        getAdapter()?.updateItems(playlists)
        binding.playlistsPlaceholder.beGoneIf(playlists.isNotEmpty())
        binding.playlistsPlaceholder2.beGoneIf(playlists.isNotEmpty())
    }

    override fun onSortOpen(activity: BaseSimpleActivity) {
        ChangeSortingDialog(activity, TAB_PLAYLISTS) {
            val adapter = getAdapter() ?: return@ChangeSortingDialog
            playlists.sortSafely(activity.config.playlistSorting)
            adapter.updateItems(playlists, forceUpdate = true)
        }
    }

    override fun setupColors(textColor: Int, adjustedPrimaryColor: Int) {
        binding.playlistsPlaceholder.setTextColor(textColor)
        binding.playlistsPlaceholder2.setTextColor(adjustedPrimaryColor)
        binding.playlistsFastscroller.updateColors(adjustedPrimaryColor)
        getAdapter()?.updateColors(textColor)
    }

    private fun getAdapter() = binding.playlistsList.adapter as? PlaylistsAdapter
}
