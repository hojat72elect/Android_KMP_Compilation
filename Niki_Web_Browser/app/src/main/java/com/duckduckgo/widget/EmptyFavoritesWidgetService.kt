

package com.duckduckgo.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.global.DuckDuckGoApplication
import com.duckduckgo.savedsites.api.SavedSitesRepository
import javax.inject.Inject

class EmptyFavoritesWidgetService : RemoteViewsService() {

    companion object {
        const val MAX_ITEMS_EXTRAS = "MAX_ITEMS_EXTRAS"
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return EmptyFavoritesWidgetItemFactory(this.applicationContext, intent)
    }

    /**
     * This RemoteViewsFactory will not render any item. It's used by is used for convenience to simplify executing background operations to show/hide empty widget CTA.
     * If this RemoteViewsFactory count is 0, SearchAndFavoritesWidget R.id.emptyfavoritesGrid will show the configured EmptyView.
     */
    class EmptyFavoritesWidgetItemFactory(
        val context: Context,
        intent: Intent,
    ) : RemoteViewsFactory {

        @Inject
        lateinit var savedSitesRepository: SavedSitesRepository

        private var count = 0

        override fun onCreate() {
            inject(context)
        }

        override fun onDataSetChanged() {
            count = if (savedSitesRepository.hasFavorites()) 1 else 0
        }

        override fun onDestroy() {
        }

        override fun getCount(): Int {
            return count
        }

        override fun getViewAt(position: Int): RemoteViews {
            return RemoteViews(context.packageName, R.layout.empty_view)
        }

        override fun getLoadingView(): RemoteViews {
            return RemoteViews(context.packageName, R.layout.empty_view)
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        private fun inject(context: Context) {
            val application = context.applicationContext as DuckDuckGoApplication
            application.daggerAppComponent.inject(this)
        }
    }
}
