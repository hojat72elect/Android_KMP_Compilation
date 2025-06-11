

package com.duckduckgo.app.browser.newtab

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.duckduckgo.app.browser.newtab.FavoritesQuickAccessAdapter.QuickAccessFavorite

class QuickAccessDragTouchItemListener(
    private val favoritesQuickAccessAdapter: FavoritesQuickAccessAdapter,
    private val dragDropListener: DragDropListener,
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
    0,
) {
    interface DragDropListener {
        fun onListChanged(listElements: List<QuickAccessFavorite>)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        val items = favoritesQuickAccessAdapter.currentList.toMutableList()
        val quickAccessFavorite = items[viewHolder.bindingAdapterPosition]
        items.removeAt(viewHolder.bindingAdapterPosition)
        items.add(target.bindingAdapterPosition, quickAccessFavorite)
        favoritesQuickAccessAdapter.submitList(items)
        return true
    }

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int,
    ) {
        // noop
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ) {
        super.clearView(recyclerView, viewHolder)
        dragDropListener.onListChanged(favoritesQuickAccessAdapter.currentList)
        (viewHolder as? DragDropViewHolderListener)?.onItemReleased()
    }

    override fun onSelectedChanged(
        viewHolder: RecyclerView.ViewHolder?,
        actionState: Int,
    ) {
        super.onSelectedChanged(viewHolder, actionState)
        val listener = viewHolder as? DragDropViewHolderListener ?: return
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> listener.onDragStarted()
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val listener = viewHolder as? DragDropViewHolderListener ?: return
        listener.onItemMoved(dX, dY)
    }
}
