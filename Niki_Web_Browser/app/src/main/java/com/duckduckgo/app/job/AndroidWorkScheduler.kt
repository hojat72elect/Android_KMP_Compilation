

package com.duckduckgo.app.job

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.notification.AndroidNotificationScheduler
import com.duckduckgo.common.utils.DefaultDispatcherProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class AndroidWorkScheduler @Inject constructor(
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val notificationScheduler: AndroidNotificationScheduler,
    private val jobCleaner: JobCleaner,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : MainProcessLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        Timber.v("Scheduling work")
        appCoroutineScope.launch(dispatcherProvider.io()) {
            jobCleaner.cleanDeprecatedJobs()
            notificationScheduler.scheduleNextNotification()
        }
    }
}
