package com.amaze.fileutilities.utilis

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amaze.fileutilities.R
import com.amaze.fileutilities.home_page.ui.ProcessingProgressView

class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @JvmField
    val processingProgressView: ProcessingProgressView =
        view.findViewById(R.id.processing_progress_view)
}
