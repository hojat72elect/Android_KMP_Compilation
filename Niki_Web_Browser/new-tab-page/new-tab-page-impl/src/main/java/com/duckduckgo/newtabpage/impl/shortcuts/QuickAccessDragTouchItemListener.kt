

package com.duckduckgo.newtabpage.impl.shortcuts

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class QuickAccessDragTouchItemListener(
    private val shortcutAdapter: ShortcutsAdapter,
    private val dragDropListener: DragDropListener,
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
    0,
) {
    interface DragDropListener {
        fun onListChanged(listElements: List<ShortcutItem>)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        val items = shortcutAdapter.currentList.toMutableList()
        val quickAccessFavorite = items[viewHolder.bindingAdapterPosition]
        items.removeAt(viewHolder.bindingAdapterPosition)
        items.add(target.bindingAdapterPosition, quickAccessFavorite)
        shortcutAdapter.submitList(items)
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
        dragDropListener.onListChanged(shortcutAdapter.currentList)
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
