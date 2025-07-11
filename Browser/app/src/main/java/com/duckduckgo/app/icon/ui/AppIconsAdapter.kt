

package com.duckduckgo.app.icon.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duckduckgo.app.browser.databinding.ItemAppIconBinding

class AppIconsAdapter(private val onClick: (ChangeIconViewModel.IconViewData) -> Unit) : RecyclerView.Adapter<AppIconsAdapter.IconViewHolder>() {

    private var iconViewData: MutableList<ChangeIconViewModel.IconViewData> = mutableListOf()

    class IconViewHolder(
        val binding: ItemAppIconBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = iconViewData.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): IconViewHolder {
        val binding = ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: IconViewHolder,
        position: Int,
    ) {
        val viewElement = iconViewData[position]
        holder.itemView.setOnClickListener { onClick.invoke(viewElement) }
        holder.binding.icon.setBackgroundResource(viewElement.appIcon.icon)
        holder.itemView.isSelected = viewElement.selected
    }

    fun notifyChanges(newList: List<ChangeIconViewModel.IconViewData>) {
        iconViewData = newList.toMutableList()
        notifyDataSetChanged()
    }
}
