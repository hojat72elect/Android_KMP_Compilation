package ca.hojat.smart.musicplayer.feature_albums

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ca.hojat.smart.musicplayer.shared.ui.dialogs.PermissionRequiredDialog
import ca.hojat.smart.musicplayer.shared.extensions.hideKeyboard
import ca.hojat.smart.musicplayer.shared.extensions.openNotificationSettings
import ca.hojat.smart.musicplayer.shared.extensions.areSystemAnimationsEnabled
import ca.hojat.smart.musicplayer.shared.extensions.getFormattedDuration
import ca.hojat.smart.musicplayer.shared.extensions.getProperPrimaryColor
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.helpers.NavigationIcon
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.shared.SimpleMusicActivity
import ca.hojat.smart.musicplayer.feature_tracks.TracksActivity
import ca.hojat.smart.musicplayer.databinding.ActivityAlbumsBinding
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.helpers.ALBUM
import ca.hojat.smart.musicplayer.shared.helpers.ARTIST
import ca.hojat.smart.musicplayer.shared.data.models.Artist
import ca.hojat.smart.musicplayer.shared.data.models.ListItem
import ca.hojat.smart.musicplayer.shared.data.models.AlbumSection
import ca.hojat.smart.musicplayer.shared.data.models.Album
import ca.hojat.smart.musicplayer.shared.data.models.Track


class AlbumsActivity : SimpleMusicActivity() {

    private val binding by viewBinding(ActivityAlbumsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        updateMaterialActivityViews(binding.albumsCoordinator, binding.albumsHolder, useTransparentNavigation = true, useTopSearchMenu = false)
        setupMaterialScrollListener(binding.albumsList, binding.albumsToolbar)

        binding.albumsFastscroller.updateColors(getProperPrimaryColor())

        val artistType = object : TypeToken<Artist>() {}.type
        val artist = Gson().fromJson<Artist>(intent.getStringExtra(ARTIST), artistType)
        binding.albumsToolbar.title = artist.title

        ensureBackgroundThread {
            val albums = audioHelper.getArtistAlbums(artist.id)
            val listItems = ArrayList<ListItem>()
            val albumsSectionLabel = resources.getQuantityString(R.plurals.albums_plural, albums.size, albums.size)
            listItems.add(AlbumSection(albumsSectionLabel))
            listItems.addAll(albums)

            val albumTracks = audioHelper.getAlbumTracks(albums)
            val trackFullDuration = albumTracks.sumOf { it.duration }

            var tracksSectionLabel = resources.getQuantityString(R.plurals.tracks_plural, albumTracks.size, albumTracks.size)
            tracksSectionLabel += " • ${trackFullDuration.getFormattedDuration(true)}"
            listItems.add(AlbumSection(tracksSectionLabel))
            listItems.addAll(albumTracks)

            runOnUiThread {
                AlbumsTracksAdapter(this, listItems, binding.albumsList) {
                    hideKeyboard()
                    if (it is Album) {
                        Intent(this, TracksActivity::class.java).apply {
                            putExtra(ALBUM, Gson().toJson(it))
                            startActivity(this)
                        }
                    } else {
                        handleNotificationPermission { granted ->
                            if (granted) {
                                val startIndex = albumTracks.indexOf(it as Track)
                                prepareAndPlay(albumTracks, startIndex)
                            } else {
                                PermissionRequiredDialog(this,
                                    R.string.allow_notifications_music_player,
                                    { openNotificationSettings() })
                            }
                        }
                    }
                }.apply {
                    binding.albumsList.adapter = this
                }

                if (areSystemAnimationsEnabled) {
                    binding.albumsList.scheduleLayoutAnimation()
                }
            }
        }

        setupCurrentTrackBar(binding.currentTrackBar.root)
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(binding.albumsToolbar, NavigationIcon.Arrow)
    }
}
