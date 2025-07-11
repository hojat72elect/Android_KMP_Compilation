package ca.hojat.smart.musicplayer.feature_genres

import android.view.View
import android.view.ViewGroup
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.ui.dialogs.ConfirmationDialog
import ca.hojat.smart.musicplayer.shared.extensions.highlightTextPart
import ca.hojat.smart.musicplayer.shared.extensions.setupViewBackground
import ca.hojat.smart.musicplayer.shared.helpers.ensureBackgroundThread
import ca.hojat.smart.musicplayer.shared.ui.views.MyRecyclerView
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.shared.ui.views.BaseMusicAdapter
import ca.hojat.smart.musicplayer.databinding.ItemGenreBinding
import ca.hojat.smart.musicplayer.shared.extensions.audioHelper
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.getGenreCoverArt
import ca.hojat.smart.musicplayer.shared.extensions.indexOfFirstOrNull
import ca.hojat.smart.musicplayer.shared.data.models.Genre
import ca.hojat.smart.musicplayer.shared.data.models.Track

class GenresAdapter(activity: BaseSimpleActivity, items: ArrayList<Genre>, recyclerView: MyRecyclerView, itemClick: (Any) -> Unit) :
    BaseMusicAdapter<Genre>(items, activity, recyclerView, itemClick), RecyclerViewFastScroller.OnPopupTextUpdate {

    override fun getActionMenuId() = R.menu.cab_genres

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGenreBinding.inflate(layoutInflater, parent, false)
        return createViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = items.getOrNull(position) ?: return
        holder.bindView(genre, allowSingleClick = true, allowLongClick = true) { itemView, _ ->
            setupView(itemView, genre)
        }
        bindViewHolder(holder)
    }

    override fun actionItemPressed(id: Int) {
        if (selectedKeys.isEmpty()) {
            return
        }

        when (id) {
            R.id.cab_add_to_playlist -> addToPlaylist()
            R.id.cab_add_to_queue -> addToQueue()
            R.id.cab_delete -> askConfirmDelete()
            R.id.cab_share -> shareFiles()
            R.id.cab_select_all -> selectAll()
        }
    }

    override fun getSelectedTracks(): ArrayList<Track> {
        return context.audioHelper.getGenreTracks(getSelectedItems())
    }

    private fun askConfirmDelete() {
        ConfirmationDialog(context) {
            ensureBackgroundThread {
                val selectedGenres = getSelectedItems()
                val positions = selectedGenres.mapNotNull { genre -> items.indexOfFirstOrNull { it.id == genre.id } } as ArrayList<Int>
                val tracks = context.audioHelper.getGenreTracks(selectedGenres)
                context.audioHelper.deleteGenres(selectedGenres)

                context.deleteTracks(tracks) {
                    context.runOnUiThread {
                        positions.sortDescending()
                        removeSelectedItems(positions)
                        positions.forEach {
                            if (items.size > it) {
                                items.removeAt(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupView(view: View, genre: Genre) {
        ItemGenreBinding.bind(view).apply {
            root.setupViewBackground(activity)
            genreFrame.isSelected = selectedKeys.contains(genre.hashCode())
            genreTitle.text = if (textToHighlight.isEmpty()) {
                genre.title
            } else {
                genre.title.highlightTextPart(textToHighlight, properPrimaryColor)
            }

            genreTitle.setTextColor(textColor)

            val tracks = resources.getQuantityString(R.plurals.tracks_plural, genre.trackCnt, genre.trackCnt)
            genreTracks.text = tracks
            genreTracks.setTextColor(textColor)

            activity.getGenreCoverArt(genre) { coverArt ->
                loadImage(genreImage, coverArt, placeholderBig)
            }
        }
    }

    override fun onChange(position: Int) = items.getOrNull(position)?.getBubbleText(context.config.genreSorting) ?: ""
}
