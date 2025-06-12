package ca.hojat.smart.musicplayer.feature_albums

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
import ca.hojat.smart.musicplayer.feature_tracks.TracksActivity
import ca.hojat.smart.musicplayer.databinding.FragmentAlbumsBinding
import ca.hojat.smart.musicplayer.shared.ui.dialogs.ChangeSortingDialog
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.mediaScanner
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.MyViewPagerFragment
import ca.hojat.smart.musicplayer.shared.helpers.ALBUM
import ca.hojat.smart.musicplayer.shared.helpers.TAB_ALBUMS
import ca.hojat.smart.musicplayer.shared.data.models.Album
import ca.hojat.smart.musicplayer.shared.data.models.sortSafely

class AlbumsFragment(context: Context, attributeSet: AttributeSet) : MyViewPagerFragment(context, attributeSet) {
    private var albums = ArrayList<Album>()
    private val binding by viewBinding(FragmentAlbumsBinding::bind)

    override fun setupFragment(activity: BaseSimpleActivity) {
        ensureBackgroundThread {
            val cachedAlbums = activity.audioHelper.getAllAlbums()
            activity.runOnUiThread {
                gotAlbums(activity, cachedAlbums)
            }
        }
    }

    private fun gotAlbums(activity: BaseSimpleActivity, cachedAlbums: ArrayList<Album>) {
        albums = cachedAlbums

        activity.runOnUiThread {
            val scanning = activity.mediaScanner.isScanning()
            binding.albumsPlaceholder.text = if (scanning) {
                context.getString(R.string.loading_files)
            } else {
                context.getString(R.string.no_items_found)
            }
            binding.albumsPlaceholder.beVisibleIf(albums.isEmpty())

            val adapter = binding.albumsList.adapter
            if (adapter == null) {
                AlbumsAdapter(activity, albums, binding.albumsList) {
                    activity.hideKeyboard()
                    Intent(activity, TracksActivity::class.java).apply {
                        putExtra(ALBUM, Gson().toJson(it))
                        activity.startActivity(this)
                    }
                }.apply {
                    binding.albumsList.adapter = this
                }

                if (context.areSystemAnimationsEnabled) {
                    binding.albumsList.scheduleLayoutAnimation()
                }
            } else {
                val oldItems = (adapter as AlbumsAdapter).items
                if (oldItems.sortedBy { it.id }.hashCode() != albums.sortedBy { it.id }.hashCode()) {
                    adapter.updateItems(albums)
                }
            }
        }
    }

    override fun finishActMode() {
        getAdapter()?.finishActMode()
    }

    override fun onSearchQueryChanged(text: String) {
        val filtered = albums.filter { it.title.contains(text, true) }.toMutableList() as ArrayList<Album>
        getAdapter()?.updateItems(filtered, text)
        binding.albumsPlaceholder.beVisibleIf(filtered.isEmpty())
    }

    override fun onSearchClosed() {
        getAdapter()?.updateItems(albums)
        binding.albumsPlaceholder.beGoneIf(albums.isNotEmpty())
    }

    override fun onSortOpen(activity: BaseSimpleActivity) {
        ChangeSortingDialog(activity, TAB_ALBUMS) {
            val adapter = getAdapter() ?: return@ChangeSortingDialog
            albums.sortSafely(activity.config.albumSorting)
            adapter.updateItems(albums, forceUpdate = true)
        }
    }

    override fun setupColors(textColor: Int, adjustedPrimaryColor: Int) {
        binding.albumsPlaceholder.setTextColor(textColor)
        binding.albumsFastscroller.updateColors(adjustedPrimaryColor)
        getAdapter()?.updateColors(textColor)
    }

    private fun getAdapter() = binding.albumsList.adapter as? AlbumsAdapter
}
