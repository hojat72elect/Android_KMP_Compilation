package ca.hojat.smart.musicplayer.shared.data

import ca.hojat.smart.musicplayer.shared.ui.views.MyRecyclerViewAdapter

interface ItemTouchHelperContract {
    fun onRowMoved(fromPosition: Int, toPosition: Int)

    fun onRowSelected(myViewHolder: MyRecyclerViewAdapter.ViewHolder?)

    fun onRowClear(myViewHolder: MyRecyclerViewAdapter.ViewHolder?)
}
