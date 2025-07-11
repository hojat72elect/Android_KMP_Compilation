

package com.duckduckgo.app.systemsearch

import android.content.pm.PackageManager.NameNotFoundException
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.duckduckgo.app.browser.databinding.ItemDeviceAppSuggestionBinding
import com.duckduckgo.app.systemsearch.DeviceAppSuggestionsAdapter.DeviceAppViewHolder

class DeviceAppSuggestionsAdapter(
    private val clickListener: (DeviceApp) -> Unit,
) : RecyclerView.Adapter<DeviceAppViewHolder>() {
    private var deviceApps: List<DeviceApp> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DeviceAppViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDeviceAppSuggestionBinding.inflate(inflater, parent, false)
        return DeviceAppViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DeviceAppViewHolder,
        position: Int,
    ) {
        holder.apply {
            val app = deviceApps[position]
            binding.title.text = app.shortName
            binding.root.setOnClickListener {
                clickListener(app)
            }
            try {
                val drawable = app.retrieveIcon(binding.icon.context.packageManager)
                binding.icon.setImageDrawable(drawable)
            } catch (e: NameNotFoundException) {
                binding.icon.setImageDrawable(null)
            }
        }
    }

    override fun getItemCount(): Int {
        return deviceApps.size
    }

    @UiThread
    fun updateData(newDeviceApps: List<DeviceApp>) {
        if (deviceApps == newDeviceApps) return
        deviceApps = newDeviceApps
        notifyDataSetChanged()
    }

    class DeviceAppViewHolder(
        val binding: ItemDeviceAppSuggestionBinding,
    ) : RecyclerView.ViewHolder(binding.root)
}
