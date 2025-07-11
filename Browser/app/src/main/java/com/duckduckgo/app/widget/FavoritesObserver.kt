

package com.duckduckgo.app.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.savedsites.api.SavedSitesRepository
import com.duckduckgo.widget.SearchAndFavoritesWidget
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.launch

@SingleInstanceIn(AppScope::class)
class FavoritesObserver @Inject constructor(
    context: Context,
    private val savedSitesRepository: SavedSitesRepository,
    private val dispatcherProvider: DispatcherProvider,
) : MainProcessLifecycleObserver {

    private val appWidgetManager: AppWidgetManager? by lazy {
        AppWidgetManager.getInstance(context)
    }
    private val componentName = ComponentName(context, SearchAndFavoritesWidget::class.java)

    override fun onStart(owner: LifecycleOwner) {
        owner.lifecycle.coroutineScope.launch(dispatcherProvider.io()) {
            appWidgetManager?.let { instance ->
                savedSitesRepository.getFavorites().collect {
                    instance.notifyAppWidgetViewDataChanged(instance.getAppWidgetIds(componentName), R.id.favoritesGrid)
                    instance.notifyAppWidgetViewDataChanged(instance.getAppWidgetIds(componentName), R.id.emptyfavoritesGrid)
                }
            }
        }
    }
}
