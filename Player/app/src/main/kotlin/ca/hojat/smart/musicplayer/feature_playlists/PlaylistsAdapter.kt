package ca.hojat.smart.musicplayer.feature_playlists

import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.shared.ui.views.BaseMusicAdapter
import ca.hojat.smart.musicplayer.databinding.ItemPlaylistBinding
import ca.hojat.smart.musicplayer.shared.ui.dialogs.PlaylistDialog
import ca.hojat.smart.musicplayer.shared.ui.dialogs.RemovePlaylistDialog
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.shared.data.models.Events
import ca.hojat.smart.musicplayer.shared.data.models.Playlist
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.highlightTextPart
import ca.hojat.smart.musicplayer.shared.extensions.indexOfFirstOrNull
import ca.hojat.smart.musicplayer.shared.extensions.setupViewBackground
import ca.hojat.smart.musicplayer.shared.ui.views.MyRecyclerView
import org.greenrobot.eventbus.EventBus

class PlaylistsAdapter(
    activity: BaseSimpleActivity, items: ArrayList<Playlist>, recyclerView: MyRecyclerView, itemClick: (Any) -> Unit
) : BaseMusicAdapter<Playlist>(items, activity, recyclerView, itemClick), RecyclerViewFastScroller.OnPopupTextUpdate {

    override fun getActionMenuId() = R.menu.cab_playlists

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlaylistBinding.inflate(layoutInflater, parent, false)
        return createViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = items.getOrNull(position) ?: return
        holder.bindView(playlist, allowSingleClick = true, allowLongClick = true) { itemView, _ ->
            setupView(itemView, playlist)
        }
        bindViewHolder(holder)
    }

    override fun prepareActionMode(menu: Menu) {
        menu.apply {
            findItem(R.id.cab_rename).isVisible = isOneItemSelected()
        }
    }

    override fun actionItemPressed(id: Int) {
        when (id) {
            R.id.cab_delete -> askConfirmDelete()
            R.id.cab_rename -> showRenameDialog()
            R.id.cab_select_all -> selectAll()
        }
    }

    private fun askConfirmDelete() {
        RemovePlaylistDialog(context) { deleteFiles ->
            val playlists = getSelectedItems().toMutableList() as ArrayList<Playlist>
            val ids = playlists.map { it.id } as ArrayList<Int>
            if (deleteFiles) {
                ensureBackgroundThread {
                    val tracksToDelete = ids.flatMap { context.audioHelper.getPlaylistTracks(it) }
                    context.deleteTracks(tracksToDelete) {
                        removePlaylists(playlists)
                    }
                }
            } else {
                removePlaylists(playlists)
            }
        }
    }

    private fun removePlaylists(playlistsToDelete: ArrayList<Playlist>) {
        val positions = playlistsToDelete.mapNotNull { playlist ->
            items.indexOfFirstOrNull { it.id == playlist.id }
        } as ArrayList<Int>

        ensureBackgroundThread {
            context.audioHelper.deletePlaylists(playlistsToDelete)
            context.runOnUiThread {
                items.removeAll(playlistsToDelete.toSet())
                removeSelectedItems(positions)
            }

            EventBus.getDefault().post(Events.PlaylistsUpdated())
        }
    }

    private fun showRenameDialog() {
        PlaylistDialog(context, items[getItemKeyPosition(selectedKeys.first())]) {
            context.runOnUiThread {
                finishActMode()
            }
        }
    }

    private fun setupView(view: View, playlist: Playlist) {
        ItemPlaylistBinding.bind(view).apply {
            root.setupViewBackground(context)
            playlistFrame.isSelected = selectedKeys.contains(playlist.hashCode())
            playlistTitle.text = if (textToHighlight.isEmpty()) playlist.title else playlist.title.highlightTextPart(textToHighlight, properPrimaryColor)
            playlistTitle.setTextColor(textColor)

            val tracks = resources.getQuantityString(R.plurals.tracks_plural, playlist.trackCount, playlist.trackCount)
            playlistTracks.text = tracks
            playlistTracks.setTextColor(textColor)
        }
    }

    override fun onChange(position: Int) = items.getOrNull(position)?.getBubbleText(context.config.playlistSorting) ?: ""
}
