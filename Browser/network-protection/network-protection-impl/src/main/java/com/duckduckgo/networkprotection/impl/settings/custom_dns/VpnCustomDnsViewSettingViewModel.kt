

package com.duckduckgo.networkprotection.impl.settings.custom_dns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duckduckgo.networkprotection.impl.VpnRemoteFeatures
import com.duckduckgo.networkprotection.impl.settings.NetPSettingsLocalConfig
import com.duckduckgo.networkprotection.impl.settings.NetpVpnSettingsDataStore
import com.duckduckgo.networkprotection.impl.settings.custom_dns.VpnCustomDnsSettingView.Event
import com.duckduckgo.networkprotection.impl.settings.custom_dns.VpnCustomDnsSettingView.Event.Init
import com.duckduckgo.networkprotection.impl.settings.custom_dns.VpnCustomDnsSettingView.State
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VpnCustomDnsViewSettingViewModel(
    private val netpVpnSettingsDataStore: NetpVpnSettingsDataStore,
    private val netPSettingsLocalConfig: NetPSettingsLocalConfig,
    private val vpnRemoteFeatures: VpnRemoteFeatures,
) : ViewModel() {

    internal fun reduce(event: Event): Flow<State> {
        return when (event) {
            Init -> onInit()
        }
    }

    private fun onInit(): Flow<State> = flow {
        netpVpnSettingsDataStore.customDns?.let {
            emit(State.CustomDns(it))
        } ?: if (netPSettingsLocalConfig.blockMalware().isEnabled() && vpnRemoteFeatures.allowDnsBlockMalware().isEnabled()) {
            emit(State.DefaultBlockMalware)
        } else {
            emit(State.Default)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val store: NetpVpnSettingsDataStore,
        private val netPSettingsLocalConfig: NetPSettingsLocalConfig,
        private val vpnRemoteFeatures: VpnRemoteFeatures,
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return with(modelClass) {
                when {
                    isAssignableFrom(VpnCustomDnsViewSettingViewModel::class.java) -> VpnCustomDnsViewSettingViewModel(
                        store,
                        netPSettingsLocalConfig,
                        vpnRemoteFeatures,
                    )

                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
        }
    }
}
