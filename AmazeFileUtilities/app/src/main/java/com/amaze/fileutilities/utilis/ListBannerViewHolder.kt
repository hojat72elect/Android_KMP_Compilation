package com.amaze.fileutilities.utilis

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amaze.fileutilities.R
import com.amaze.fileutilities.home_page.ui.media_tile.MediaTypeHeaderView

class ListBannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @JvmField
    val mediaTypeHeaderView: MediaTypeHeaderView = view.findViewById(R.id.listBannerView)
}
