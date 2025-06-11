

package com.duckduckgo.app.global.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duckduckgo.common.ui.view.divider.HorizontalDivider

class DividerAdapter : RecyclerView.Adapter<DividerAdapter.DividerViewHolder>() {

    class DividerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DividerViewHolder {
        return DividerViewHolder(HorizontalDivider(parent.context))
    }

    override fun onBindViewHolder(
        holder: DividerViewHolder,
        position: Int,
    ) {
        // noop
    }

    override fun getItemCount(): Int = 1
}
