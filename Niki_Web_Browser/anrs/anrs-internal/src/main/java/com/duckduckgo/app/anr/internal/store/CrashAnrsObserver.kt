

package com.duckduckgo.app.anr.internal.store

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.anr.internal.setting.CrashANRsRepository
import com.duckduckgo.app.anrs.store.AnrsDatabase
import com.duckduckgo.app.anrs.store.UncaughtExceptionDao
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.lifecycle.VpnProcessLifecycleObserver
import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import logcat.logcat
import okio.ByteString.Companion.encode

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = VpnProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class CrashAnrsObserver @Inject constructor(
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val coroutineDispatcher: DispatcherProvider,
    private val repository: CrashANRsRepository,
    private val anrDatabase: AnrsDatabase,
    private val uncaughtExceptionDao: UncaughtExceptionDao,
) : MainProcessLifecycleObserver, VpnProcessLifecycleObserver {

    private var anrObserverJob: ConflatedJob = ConflatedJob()
    private var crashObserverJob: ConflatedJob = ConflatedJob()

    override fun onCreate(owner: LifecycleOwner) {
        anrObserverJob += observeAnrDatabase()
        crashObserverJob += observeCrashDatabase()
    }

    override fun onVpnProcessCreated() {
        anrObserverJob += observeAnrDatabase()
        crashObserverJob += observeCrashDatabase()
    }

    private fun observeAnrDatabase() = appCoroutineScope.launch(coroutineDispatcher.io()) {
        logcat { "Observing ANRs database..." }
        anrDatabase.arnDao().getAnrsFlow().drop(1).distinctUntilChanged().flowOn(coroutineDispatcher.io()).collect { anrs ->
            anrs.forEach { anr ->
                val stacktraceAsString = anr.stackTrace.joinToString("\n")
                repository.insertANR(
                    AnrInternalEntity(
                        hash = (stacktraceAsString + anr.timestamp).encode().md5().hex(),
                        stackTrace = stacktraceAsString,
                        timestamp = anr.timestamp,
                        message = anr.message,
                        name = anr.name,
                        file = anr.file,
                        lineNumber = anr.lineNumber,
                        webView = anr.webView,
                        customTab = anr.customTab,
                    ),
                )
            }
        }
    }

    private fun observeCrashDatabase() = appCoroutineScope.launch(coroutineDispatcher.io()) {
        logcat { "Observing Crashes database..." }
        uncaughtExceptionDao.allFlow().drop(1).distinctUntilChanged().flowOn(coroutineDispatcher.io()).collect { crashes ->
            crashes.forEach { crash ->
                repository.insertCrash(
                    CrashInternalEntity(
                        hash = (crash.stackTrace + crash.timestamp).encode().md5().hex(),
                        stackTrace = crash.stackTrace,
                        timestamp = crash.timestamp,
                        shortName = crash.shortName,
                        processName = crash.processName,
                        message = crash.message,
                        version = crash.version,
                        webView = crash.webView,
                        customTab = crash.customTab,
                    ),
                )
            }
        }
    }
}
