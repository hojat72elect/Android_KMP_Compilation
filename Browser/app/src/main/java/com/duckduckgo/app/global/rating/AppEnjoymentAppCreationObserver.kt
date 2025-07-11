

package com.duckduckgo.app.global.rating

import androidx.annotation.UiThread
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DefaultDispatcherProvider
import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppEnjoymentAppCreationObserver(
    private val appEnjoymentPromptEmitter: AppEnjoymentPromptEmitter,
    private val promptTypeDecider: PromptTypeDecider,
    private val appCoroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider(),
) : MainProcessLifecycleObserver {

    @UiThread
    override fun onStart(owner: LifecycleOwner) {
        appCoroutineScope.launch(dispatchers.main()) {
            appEnjoymentPromptEmitter.promptType.value = promptTypeDecider.determineInitialPromptType()
        }
    }
}
