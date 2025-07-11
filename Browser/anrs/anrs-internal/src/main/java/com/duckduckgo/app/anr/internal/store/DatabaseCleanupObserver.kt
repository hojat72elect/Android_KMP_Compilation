

package com.duckduckgo.app.anr.internal.store

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class DatabaseCleanupObserver @Inject constructor(
    private val internalDatabase: CrashANRsInternalDatabase,
    @AppCoroutineScope private val appScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        appScope.launch(dispatcherProvider.io()) {
            val removeBeforeTimestamp = DatabaseDateFormatter.timestamp(LocalDateTime.now().minusDays(30))
            internalDatabase.anrDao().removeOldAnrs(removeBeforeTimestamp)
            internalDatabase.crashDao().removeOldCrashes(removeBeforeTimestamp)
        }
    }
}
