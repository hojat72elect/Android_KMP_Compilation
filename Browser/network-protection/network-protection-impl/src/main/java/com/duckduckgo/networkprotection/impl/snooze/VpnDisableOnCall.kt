

package com.duckduckgo.networkprotection.impl.snooze

import android.content.Context
import android.content.Intent
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.networkprotection.impl.settings.NetPSettingsLocalConfig
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface VpnDisableOnCall {
    fun enable()
    fun disable()

    suspend fun isEnabled(): Boolean
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealVpnDisableOnCall @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val context: Context,
    private val netPSettingsLocalConfig: NetPSettingsLocalConfig,
) : VpnDisableOnCall {

    override fun enable() {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            netPSettingsLocalConfig.vpnPauseDuringCalls().setRawStoredState(Toggle.State(enable = true))
            context.sendBroadcast(Intent(VpnCallStateReceiver.ACTION_REGISTER_STATE_CALL_LISTENER))
        }
    }

    override fun disable() {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            context.sendBroadcast(Intent(VpnCallStateReceiver.ACTION_UNREGISTER_STATE_CALL_LISTENER))
            netPSettingsLocalConfig.vpnPauseDuringCalls().setRawStoredState(Toggle.State(enable = false))
        }
    }

    override suspend fun isEnabled(): Boolean = withContext(dispatcherProvider.io()) {
        return@withContext netPSettingsLocalConfig.vpnPauseDuringCalls().isEnabled()
    }
}
