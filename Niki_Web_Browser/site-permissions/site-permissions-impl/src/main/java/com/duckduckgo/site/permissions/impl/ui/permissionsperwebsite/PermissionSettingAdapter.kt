

package com.duckduckgo.site.permissions.impl.ui.permissionsperwebsite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duckduckgo.site.permissions.impl.databinding.ItemSitePermissionSettingSelectionBinding
import com.duckduckgo.site.permissions.impl.ui.permissionsperwebsite.PermissionSettingAdapter.ViewHolder

class PermissionSettingAdapter(private val viewModel: PermissionsPerWebsiteViewModel) : RecyclerView.Adapter<ViewHolder>() {

    private var items: List<WebsitePermissionSetting> = listOf()

    fun updateItems(settings: List<WebsitePermissionSetting>) {
        items = settings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder =
        ViewHolder(
            ItemSitePermissionSettingSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel,
        )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemSitePermissionSettingSelectionBinding,
        private val viewModel: PermissionsPerWebsiteViewModel,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(setting: WebsitePermissionSetting) {
            val context = binding.root.context
            binding.permissionSettingListItem.apply {
                setLeadingIconResource(setting.icon)
                setPrimaryText(context.getString(setting.title))
                setSecondaryText(context.getString(setting.setting.stringRes))
                setOnClickListener { viewModel.permissionSettingSelected(setting) }
            }
        }
    }
}
