package com.duckduckgo.app.flipper

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.squareup.anvil.annotations.ContributesMultibinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class FlipperInitializer @Inject constructor(
    private val context: Context,
    private val flipperPluginPoint: PluginPoint<FlipperPlugin>,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        Timber.v("Flipper: setup flipper")
        SoLoader.init(context, false)

        coroutineScope.launch(dispatcherProvider.io()) {
            with(AndroidFlipperClient.getInstance(context)) {
                flipperPluginPoint.getPlugins().forEach { plugin ->
                    addPlugin(plugin)
                }

                // Common device plugins
                addPlugin(DatabasesFlipperPlugin(context))

                start()
            }
        }
    }
}
