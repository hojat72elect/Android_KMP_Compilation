

package com.duckduckgo.app.feedback.ui.negative.subreason

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.duckduckgo.app.feedback.ui.negative.FeedbackTypeDisplay.FeedbackTypeSubReasonDisplay
import com.duckduckgo.mobile.android.databinding.RowOneLineListItemBinding

class SubReasonAdapter(private val itemClickListener: (FeedbackTypeSubReasonDisplay) -> Unit) :
    ListAdapter<FeedbackTypeSubReasonDisplay, SubReasonAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<FeedbackTypeSubReasonDisplay>() {
        override fun areItemsTheSame(
            oldItem: FeedbackTypeSubReasonDisplay,
            newItem: FeedbackTypeSubReasonDisplay,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FeedbackTypeSubReasonDisplay,
            newItem: FeedbackTypeSubReasonDisplay,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowOneLineListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position), itemClickListener)
    }

    data class ViewHolder(val binding: RowOneLineListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            reason: FeedbackTypeSubReasonDisplay,
            clickListener: (FeedbackTypeSubReasonDisplay) -> Unit,
        ) {
            val listItem = binding.root
            listItem.setLeadingIconVisibility(false)
            listItem.setPrimaryText(binding.root.context.getString(reason.listDisplayResId))
            listItem.setOnClickListener { clickListener(reason) }
        }
    }
}
