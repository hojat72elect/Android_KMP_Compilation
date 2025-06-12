package com.amaze.fileutilities.utilis

import android.content.Context
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.amaze.fileutilities.R

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // each data item is just a string in this case
    private val txtTitle: TextView = view.findViewById(R.id.header_title)
    private val summaryTextView: TextView = view.findViewById(R.id.header_summary)
    private val overflowButton: ImageView = view.findViewById(R.id.overflow_button)

    fun setText(headerText: String) {
        txtTitle.text = headerText
        Utils.marqueeAfterDelay(3000, txtTitle)
    }

    fun setSummaryText(summaryText: String) {
        summaryTextView.visibility = View.VISIBLE
        summaryTextView.text = summaryText
    }

    fun setOverflowButtons(
        context: Context,
        menuRes: Int,
        onMenuItemClickListener: PopupMenu.OnMenuItemClickListener
    ) {
        val overflowContext = ContextThemeWrapper(context, R.style.custom_action_mode_dark)
        val popupMenu = androidx.appcompat.widget.PopupMenu(
            overflowContext, overflowButton, Gravity.END
        )
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            onMenuItemClickListener.onMenuItemClick(item)
        }
        popupMenu.menuInflater.inflate(menuRes, popupMenu.menu)
        overflowButton.setOnClickListener {
            popupMenu.show()
        }
    }
}
