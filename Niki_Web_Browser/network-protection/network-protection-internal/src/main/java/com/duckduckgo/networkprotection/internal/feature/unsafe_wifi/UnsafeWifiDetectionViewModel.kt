

package com.duckduckgo.networkprotection.internal.feature.unsafe_wifi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duckduckgo.networkprotection.internal.feature.unsafe_wifi.UnsafeWifiDetectionSettingView.Event
import com.duckduckgo.networkprotection.internal.feature.unsafe_wifi.UnsafeWifiDetectionSettingView.Event.Init
import com.duckduckgo.networkprotection.internal.feature.unsafe_wifi.UnsafeWifiDetectionSettingView.Event.OnDisableIntent
import com.duckduckgo.networkprotection.internal.feature.unsafe_wifi.UnsafeWifiDetectionSettingView.Event.OnEnableIntent
import com.duckduckgo.networkprotection.internal.feature.unsafe_wifi.UnsafeWifiDetectionSettingView.State
import com.duckduckgo.networkprotection.internal.feature.unsafe_wifi.UnsafeWifiDetectionSettingView.State.Disable
import com.duckduckgo.networkprotection.internal.feature.unsafe_wifi.UnsafeWifiDetectionSettingView.State.Enable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UnsafeWifiDetectionViewModel(
    private val unsafeWifiMonitor: UnsafeWifiMonitor,
) : ViewModel() {

    internal fun reduce(event: Event): Flow<State> {
        return when (event) {
            OnDisableIntent -> onDisableIntent()
            OnEnableIntent -> onEnableIntent()
            Init -> onInit()
        }
    }

    private fun onInit(): Flow<State> = flow {
        if (unsafeWifiMonitor.isEnabled()) {
            emit(Enable)
        } else {
            emit(Disable)
        }
    }

    private fun onEnableIntent(): Flow<State> = flow {
        unsafeWifiMonitor.enable()
        onInit()
    }

    private fun onDisableIntent(): Flow<State> = flow {
        unsafeWifiMonitor.disable()
        onInit()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val unsafeWifiMonitor: UnsafeWifiMonitor,
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return with(modelClass) {
                when {
                    isAssignableFrom(UnsafeWifiDetectionViewModel::class.java) -> UnsafeWifiDetectionViewModel(
                        unsafeWifiMonitor,
                    )
                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
        }
    }
}
