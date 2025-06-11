

package com.duckduckgo.widget

import android.content.Context
import com.duckduckgo.common.ui.view.toDp
import com.duckduckgo.mobile.android.R as CommonR
import timber.log.Timber

class SearchAndFavoritesGridCalculator {
    fun calculateColumns(
        context: Context,
        width: Int,
    ): Int {
        val margins = context.resources.getDimension(CommonR.dimen.searchWidgetFavoritesSideMargin).toDp()
        val item = context.resources.getDimension(CommonR.dimen.searchWidgetFavoriteItemContainerWidth).toDp()
        val divider = context.resources.getDimension(CommonR.dimen.searchWidgetFavoritesHorizontalSpacing).toDp()
        var n = 2
        var totalSize = (n * item) + ((n - 1) * divider) + (margins * 2)

        Timber.i("SearchAndFavoritesWidget width n:$n $totalSize vs $width")
        while (totalSize <= width) {
            ++n
            totalSize = (n * item) + ((n - 1) * divider) + (margins * 2)
            Timber.i("SearchAndFavoritesWidget width n:$n $totalSize vs $width")
        }

        return WIDGET_COLUMNS_MIN.coerceAtLeast(n - 1)
    }

    fun calculateRows(
        context: Context,
        height: Int,
    ): Int {
        val searchBar = context.resources.getDimension(CommonR.dimen.searchWidgetSearchBarHeight).toDp()
        val margins = context.resources.getDimension(CommonR.dimen.searchWidgetFavoritesTopMargin).toDp() +
            (context.resources.getDimension(CommonR.dimen.searchWidgetPadding).toDp() * 2)
        val item = context.resources.getDimension(CommonR.dimen.searchWidgetFavoriteItemContainerHeight).toDp()
        val divider = context.resources.getDimension(CommonR.dimen.searchWidgetFavoritesVerticalSpacing).toDp()
        var n = 1
        var totalSize = searchBar + (n * item) + ((n - 1) * divider) + margins

        Timber.i("SearchAndFavoritesWidget height n:$n $totalSize vs $height")
        while (totalSize <= height) {
            ++n
            totalSize = searchBar + (n * item) + ((n - 1) * divider) + margins
            Timber.i("SearchAndFavoritesWidget height n:$n $totalSize vs $height")
        }

        var rows = n - 1
        rows = WIDGET_ROWS_MIN.coerceAtLeast(rows)
        rows = WIDGET_ROWS_MAX.coerceAtMost(rows)
        return rows
    }

    companion object {
        private const val WIDGET_COLUMNS_MIN = 2
        private const val WIDGET_ROWS_MAX = 4
        private const val WIDGET_ROWS_MIN = 1
    }
}
