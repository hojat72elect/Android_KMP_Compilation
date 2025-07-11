package ca.hojat.smart.musicplayer.feature_artists

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import com.google.gson.Gson
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.extensions.areSystemAnimationsEnabled
import ca.hojat.smart.musicplayer.shared.extensions.beGoneIf
import ca.hojat.smart.musicplayer.shared.extensions.beVisibleIf
import ca.hojat.smart.musicplayer.shared.extensions.hideKeyboard
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.feature_albums.AlbumsActivity
import ca.hojat.smart.musicplayer.databinding.FragmentArtistsBinding
import ca.hojat.smart.musicplayer.shared.ui.dialogs.ChangeSortingDialog
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.mediaScanner
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.MyViewPagerFragment
import ca.hojat.smart.musicplayer.shared.helpers.ARTIST
import ca.hojat.smart.musicplayer.shared.helpers.TAB_ARTISTS
import ca.hojat.smart.musicplayer.shared.data.models.Artist
import ca.hojat.smart.musicplayer.shared.data.models.sortSafely

// Artists -> Albums -> Tracks
class ArtistsFragment(context: Context, attributeSet: AttributeSet) : MyViewPagerFragment(context, attributeSet) {
    private var artists = ArrayList<Artist>()
    private val binding by viewBinding(FragmentArtistsBinding::bind)

    override fun setupFragment(activity: BaseSimpleActivity) {
        ensureBackgroundThread {
            val cachedArtists = activity.audioHelper.getAllArtists()
            activity.runOnUiThread {
                gotArtists(activity, cachedArtists)
            }
        }
    }

    private fun gotArtists(activity: BaseSimpleActivity, cachedArtists: ArrayList<Artist>) {
        artists = cachedArtists
        activity.runOnUiThread {
            val scanning = activity.mediaScanner.isScanning()
            binding.artistsPlaceholder.text = if (scanning) {
                context.getString(R.string.loading_files)
            } else {
                context.getString(R.string.no_items_found)
            }
            binding.artistsPlaceholder.beVisibleIf(artists.isEmpty())

            val adapter = binding.artistsList.adapter
            if (adapter == null) {
                ArtistsAdapter(activity, artists, binding.artistsList) {
                    activity.hideKeyboard()
                    Intent(activity, AlbumsActivity::class.java).apply {
                        putExtra(ARTIST, Gson().toJson(it as Artist))
                        activity.startActivity(this)
                    }
                }.apply {
                    binding.artistsList.adapter = this
                }

                if (context.areSystemAnimationsEnabled) {
                    binding.artistsList.scheduleLayoutAnimation()
                }
            } else {
                val oldItems = (adapter as ArtistsAdapter).items
                if (oldItems.sortedBy { it.id }.hashCode() != artists.sortedBy { it.id }.hashCode()) {
                    adapter.updateItems(artists)
                }
            }
        }
    }

    override fun finishActMode() {
        getAdapter()?.finishActMode()
    }

    override fun onSearchQueryChanged(text: String) {
        val filtered = artists.filter { it.title.contains(text, true) }.toMutableList() as ArrayList<Artist>
        getAdapter()?.updateItems(filtered, text)
        binding.artistsPlaceholder.beVisibleIf(filtered.isEmpty())
    }

    override fun onSearchClosed() {
        getAdapter()?.updateItems(artists)
        binding.artistsPlaceholder.beGoneIf(artists.isNotEmpty())
    }

    override fun onSortOpen(activity: BaseSimpleActivity) {
        ChangeSortingDialog(activity, TAB_ARTISTS) {
            val adapter = getAdapter() ?: return@ChangeSortingDialog
            artists.sortSafely(activity.config.artistSorting)
            adapter.updateItems(artists, forceUpdate = true)
        }
    }

    override fun setupColors(textColor: Int, adjustedPrimaryColor: Int) {
        binding.artistsPlaceholder.setTextColor(textColor)
        binding.artistsFastscroller.updateColors(adjustedPrimaryColor)
        getAdapter()?.updateColors(textColor)
    }

    private fun getAdapter() = binding.artistsList.adapter as? ArtistsAdapter
}
