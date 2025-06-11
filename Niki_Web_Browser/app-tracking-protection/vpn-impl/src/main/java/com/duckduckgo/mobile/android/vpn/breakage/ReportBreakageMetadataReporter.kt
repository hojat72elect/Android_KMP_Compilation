

package com.duckduckgo.mobile.android.vpn.breakage

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollector
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface ReportBreakageMetadataReporter {
    suspend fun getVpnStateMetadata(appPackageId: String? = null): String
}

@ContributesBinding(ActivityScope::class)
class RealReportBreakageMetadataReporter @Inject constructor(
    private val vpnStateCollector: VpnStateCollector,
    private val dispatcherProvider: DispatcherProvider,
) : ReportBreakageMetadataReporter {
    override suspend fun getVpnStateMetadata(appPackageId: String?): String {
        return withContext(dispatcherProvider.io()) {
            val state = vpnStateCollector.collectVpnState(appPackageId)
            return@withContext state.toString()
        }
    }
}
