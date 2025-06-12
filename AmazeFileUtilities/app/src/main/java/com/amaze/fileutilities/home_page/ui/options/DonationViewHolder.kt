package com.amaze.fileutilities.home_page.ui.options

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amaze.fileutilities.R

class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    val ROOT_VIEW: LinearLayout = itemView.findViewById(R.id.adapter_donation_root)

    @JvmField
    val TITLE: TextView = itemView.findViewById(R.id.adapter_donation_title)

    @JvmField
    val SUMMARY: TextView = itemView.findViewById(R.id.adapter_donation_summary)

    @JvmField
    val PRICE: TextView = itemView.findViewById(R.id.adapter_donation_price)

    @JvmField
    val RENEWAL_CYCLE: TextView = itemView.findViewById(R.id.adapter_donation_cycle)
}
