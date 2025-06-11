

package com.duckduckgo.mobile.android.vpn.cohort

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.appbuildconfig.api.isInternalBuild
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.checkMainThread
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.AppTpVpnFeature
import com.duckduckgo.mobile.android.vpn.VpnFeaturesRegistry
import com.duckduckgo.mobile.android.vpn.service.VpnServiceCallbacks
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface CohortStore {
    /**
     * @return the stored cohort local date or [null] if never set
     */
    @WorkerThread
    fun getCohortStoredLocalDate(): LocalDate?

    /**
     * Stores the cohort [LocalDate] passed as parameter
     */
    @WorkerThread
    fun setCohortLocalDate(localDate: LocalDate)
}

@ContributesBinding(
    scope = AppScope::class,
    boundType = CohortStore::class,
)
@ContributesMultibinding(
    scope = VpnScope::class,
    boundType = VpnServiceCallbacks::class,
)
class RealCohortStore @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
    private val vpnFeaturesRegistry: VpnFeaturesRegistry,
    private val dispatcherProvider: DispatcherProvider,
    private val appBuildConfig: AppBuildConfig,
) : CohortStore, VpnServiceCallbacks {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(
            FILENAME,
            multiprocess = true,
            migrate = true,
        )
    }

    override fun getCohortStoredLocalDate(): LocalDate? {
        if (appBuildConfig.isInternalBuild()) {
            checkMainThread()
        }

        return preferences.getString(KEY_COHORT_LOCAL_DATE, null)?.let {
            LocalDate.parse(it)
        }
    }

    override fun setCohortLocalDate(localDate: LocalDate) {
        if (appBuildConfig.isInternalBuild()) {
            checkMainThread()
        }

        preferences.edit { putString(KEY_COHORT_LOCAL_DATE, formatter.format(localDate)) }
    }

    override fun onVpnStarted(coroutineScope: CoroutineScope) {
        coroutineScope.launch(dispatcherProvider.io()) {
            attemptAssignCohort()
        }
    }

    override fun onVpnReconfigured(coroutineScope: CoroutineScope) {
        coroutineScope.launch(dispatcherProvider.io()) {
            attemptAssignCohort()
        }
    }

    override fun onVpnStopped(
        coroutineScope: CoroutineScope,
        vpnStopReason: VpnStopReason,
    ) {
        // noop
    }

    private suspend fun attemptAssignCohort() {
        if (vpnFeaturesRegistry.isFeatureRegistered(AppTpVpnFeature.APPTP_VPN)) {
            // skip if already stored
            getCohortStoredLocalDate()?.let { return }

            setCohortLocalDate(LocalDate.now())
        }
    }

    companion object {
        private const val FILENAME = "com.duckduckgo.mobile.atp.cohort.prefs"
        private const val KEY_COHORT_LOCAL_DATE = "KEY_COHORT_LOCAL_DATE"
    }
}
