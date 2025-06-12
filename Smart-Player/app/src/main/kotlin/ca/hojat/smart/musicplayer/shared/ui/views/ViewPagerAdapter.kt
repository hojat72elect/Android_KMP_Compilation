package ca.hojat.smart.musicplayer.shared.ui.views

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import ca.hojat.smart.musicplayer.databinding.FragmentAlbumsBinding
import ca.hojat.smart.musicplayer.databinding.FragmentArtistsBinding
import ca.hojat.smart.musicplayer.databinding.FragmentFoldersBinding
import ca.hojat.smart.musicplayer.databinding.FragmentGenresBinding
import ca.hojat.smart.musicplayer.databinding.FragmentPlaylistsBinding
import ca.hojat.smart.musicplayer.databinding.FragmentTracksBinding
import ca.hojat.smart.musicplayer.shared.extensions.getProperPrimaryColor
import ca.hojat.smart.musicplayer.shared.extensions.getProperTextColor
import ca.hojat.smart.musicplayer.shared.extensions.getVisibleTabs
import ca.hojat.smart.musicplayer.shared.MyViewPagerFragment
import ca.hojat.smart.musicplayer.shared.helpers.TAB_ALBUMS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_ARTISTS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_FOLDERS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_GENRES
import ca.hojat.smart.musicplayer.shared.helpers.TAB_PLAYLISTS
import ca.hojat.smart.musicplayer.shared.helpers.TAB_TRACKS
import ca.hojat.smart.musicplayer.feature_playlists.PlaylistsFragment
import ca.hojat.smart.musicplayer.feature_tracks.TracksFragment
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity


class ViewPagerAdapter(val activity: BaseSimpleActivity) : PagerAdapter() {
    private val fragments = arrayListOf<MyViewPagerFragment>()
    private var primaryItem: MyViewPagerFragment? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return getFragment(position, container).apply {
            fragments.add(this)
            container.addView(this)
            setupFragment(activity)
            setupColors(activity.getProperTextColor(), activity.getProperPrimaryColor())
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        fragments.remove(item)
        container.removeView(item as View)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        primaryItem = `object` as MyViewPagerFragment
    }

    override fun getCount() = activity.getVisibleTabs().size

    override fun isViewFromObject(view: View, item: Any) = view == item

    private fun getFragment(position: Int, container: ViewGroup): MyViewPagerFragment {
        val tab = activity.getVisibleTabs()[position]
        val layoutInflater = activity.layoutInflater
        return when (tab) {
            TAB_PLAYLISTS -> FragmentPlaylistsBinding.inflate(layoutInflater, container, false).root
            TAB_FOLDERS -> FragmentFoldersBinding.inflate(layoutInflater, container, false).root
            TAB_ARTISTS -> FragmentArtistsBinding.inflate(layoutInflater, container, false).root
            TAB_ALBUMS -> FragmentAlbumsBinding.inflate(layoutInflater, container, false).root
            TAB_TRACKS -> FragmentTracksBinding.inflate(layoutInflater, container, false).root
            TAB_GENRES -> FragmentGenresBinding.inflate(layoutInflater, container, false).root
            else -> throw IllegalArgumentException("Unknown tab: $tab")
        }
    }

    fun getAllFragments() = fragments

    fun getCurrentFragment() = primaryItem

    fun getPlaylistsFragment() = fragments.find { it is PlaylistsFragment }

    fun getTracksFragment() = fragments.find { it is TracksFragment }
}
