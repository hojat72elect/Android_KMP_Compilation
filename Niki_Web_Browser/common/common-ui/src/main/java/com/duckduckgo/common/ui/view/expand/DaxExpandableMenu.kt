

package com.duckduckgo.common.ui.view.expand

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import com.duckduckgo.mobile.android.R

class DaxExpandableMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.daxExpandableMenuItemStyle,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var expandedId: Int = -1
    private var onExpandedChangedListener = ExpandedStateTracker()

    override fun onFinishInflate() {
        super.onFinishInflate()

        // find all DaxExpandableMenuItems inside
        val menuItems = children.filter { it is DaxExpandableMenuItem }.map { it as DaxExpandableMenuItem }
        menuItems.forEach {
            it.setExpandedChangeListener(onExpandedChangedListener)
        }
    }

    private fun toggleExpandedStateForView(viewId: Int) {
        val expandedView = findViewById<DaxExpandableMenuItem>(viewId)
        expandedView.toggle()
    }

    private inner class ExpandedStateTracker : OnExpandedChangedListener {

        override fun onExpandedChange(view: View, expanded: Boolean) {
            if (expandedId != -1) {
                if (expanded && expandedId != view.id) {
                    toggleExpandedStateForView(expandedId)
                }
            }
            expandedId = if (expanded) {
                view.id
            } else {
                -1
            }
        }
    }

    /** Builder class for creating [DaxExpandableMenu]. */
    @DaxExpandableMenuDsl
    class Builder(context: Context)
}
