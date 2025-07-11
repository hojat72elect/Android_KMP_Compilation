

package com.duckduckgo.experiments.impl.reinstalls

import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.app.statistics.AtbInitializerListener
import com.duckduckgo.app.statistics.store.StatisticsDataStore
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

@SingleInstanceIn(AppScope::class)
@ContributesMultibinding(AppScope::class)
@PriorityKey(AtbInitializerListener.PRIORITY_REINSTALL_LISTENER)
class ReinstallAtbListener @Inject constructor(
    private val backupDataStore: BackupServiceDataStore,
    private val statisticsDataStore: StatisticsDataStore,
    private val appBuildConfig: AppBuildConfig,
    private val dispatcherProvider: DispatcherProvider,
) : AtbInitializerListener {

    override suspend fun beforeAtbInit() = withContext(dispatcherProvider.io()) {
        backupDataStore.clearBackupPreferences()

        if (appBuildConfig.isAppReinstall()) {
            statisticsDataStore.variant = REINSTALL_VARIANT
            Timber.i("Variant update for returning user")
        }
    }

    override fun beforeAtbInitTimeoutMillis(): Long = MAX_REINSTALL_WAIT_TIME_MS

    companion object {
        private const val MAX_REINSTALL_WAIT_TIME_MS = 1_500L
    }
}

internal const val REINSTALL_VARIANT = "ru"
