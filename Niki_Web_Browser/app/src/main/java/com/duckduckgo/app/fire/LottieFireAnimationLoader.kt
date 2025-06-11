

package com.duckduckgo.app.fire

import android.content.Context
import androidx.annotation.UiThread
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieCompositionFactory
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface FireAnimationLoader : MainProcessLifecycleObserver {
    fun preloadSelectedAnimation()
}

class LottieFireAnimationLoader constructor(
    private val context: Context,
    private val settingsDataStore: SettingsDataStore,
    private val dispatchers: DispatcherProvider,
    private val appCoroutineScope: CoroutineScope,
) : FireAnimationLoader {

    override fun onCreate(owner: LifecycleOwner) {
        preloadSelectedAnimation()
    }

    @UiThread
    override fun preloadSelectedAnimation() {
        appCoroutineScope.launch(dispatchers.io()) {
            if (animationEnabled()) {
                val selectedFireAnimation = settingsDataStore.selectedFireAnimation
                LottieCompositionFactory.fromRawRes(context, selectedFireAnimation.resId)
            }
        }
    }

    private fun animationEnabled() = settingsDataStore.fireAnimationEnabled
}
