

package com.duckduckgo.app.tabs.ui

import android.content.Context
import com.duckduckgo.common.ui.view.toDp
import com.duckduckgo.common.ui.view.toPx
import kotlin.math.min

class GridViewColumnCalculator(val context: Context) {

    fun calculateNumberOfColumns(
        columnWidthDp: Int,
        maxColumns: Int,
    ): Int {
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels.toDp()
        val numberOfColumns = screenWidthDp / columnWidthDp
        return min(maxColumns, numberOfColumns).coerceAtLeast(1)
    }

    /**
     * Given a numOfColumns and their width, calculate sides padding to center all items on the GridView.
     * RecyclerView should have a match_parent width to avoid clipping items if drag-drop enabled.
     *
     * @return start/end padding in pixels
     */
    fun calculateSidePadding(
        columnWidthDp: Int,
        numOfColumns: Int,
    ): Int {
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels.toDp()
        val columnsWidth = columnWidthDp * numOfColumns
        val remainingSpace = screenWidthDp - columnsWidth
        return if (remainingSpace <= 0) 0 else (remainingSpace / 2).toPx()
    }
}
