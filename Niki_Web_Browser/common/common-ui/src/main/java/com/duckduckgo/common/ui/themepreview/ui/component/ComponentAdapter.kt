

package com.duckduckgo.common.ui.themepreview.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ComponentAdapter : ListAdapter<Component, ComponentViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = getItem(position).ordinal

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ComponentViewHolder {
        return ComponentViewHolder.create(parent, viewType)
    }

    override fun onBindViewHolder(
        holder: ComponentViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Component>() {
                override fun areItemsTheSame(
                    oldItem: Component,
                    newItem: Component,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Component,
                    newItem: Component,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
