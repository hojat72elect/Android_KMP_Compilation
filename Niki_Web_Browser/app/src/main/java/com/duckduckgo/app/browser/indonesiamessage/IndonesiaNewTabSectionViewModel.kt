

package com.duckduckgo.app.browser.indonesiamessage

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ViewScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("NoLifecycleObserver") // we don't observe app lifecycle
@ContributesViewModel(ViewScope::class)
class IndonesiaNewTabSectionViewModel @Inject constructor(
    private val indonesiaNewTabSectionDataStore: IndonesiaNewTabSectionDataStore,
    private val dispatchers: DispatcherProvider,
    private val applicationContext: Context,
) : ViewModel(), DefaultLifecycleObserver {

    data class ViewState(val showMessage: Boolean = false)

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        viewModelScope.launch(dispatchers.io()) {
            indonesiaNewTabSectionDataStore.updateShowMessage(MAX_DAYS_MESSAGE_SHOWN)

            if (!indonesiaNewTabSectionDataStore.showMessage.first()) {
                _viewState.value = ViewState(false)
                return@launch
            }

            val mcc = applicationContext.resources.configuration.mcc
            val showMessage = when (mcc) {
                MCC_INDONESIA -> true
                MCC_UNDEFINED -> runCatching {
                    val telephonyManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val networkCountryIso = telephonyManager.networkCountryIso
                    networkCountryIso.lowercase() == NETWORK_COUNTRY_ISO_INDONESIA
                }.getOrDefault(false)

                else -> false
            }
            _viewState.value = ViewState(showMessage)
        }
    }

    fun onMessageDismissed() {
        viewModelScope.launch(dispatchers.io()) {
            indonesiaNewTabSectionDataStore.dismissMessage()
        }
    }

    companion object {
        internal const val MCC_INDONESIA = 510
        internal const val MCC_UNDEFINED = 0
        internal const val NETWORK_COUNTRY_ISO_INDONESIA = "id"
        internal const val MAX_DAYS_MESSAGE_SHOWN = 7
    }
}
