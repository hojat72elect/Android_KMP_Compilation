

package com.duckduckgo.app.userstate

import android.content.Context
import androidx.annotation.UiThread
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.tabs.model.TabDataRepository
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class UserStateReporter @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val repository: TabDataRepository,
    private val context: Context,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
) : MainProcessLifecycleObserver {

    @UiThread
    override fun onCreate(owner: LifecycleOwner) {
        appCoroutineScope.launch(dispatchers.io()) {
            val firstInstallTime: Long = context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
            val lastUpdateTime: Long = context.packageManager.getPackageInfo(context.packageName, 0).lastUpdateTime

            repository.setIsUserNew(firstInstallTime == lastUpdateTime)
        }
    }
}
