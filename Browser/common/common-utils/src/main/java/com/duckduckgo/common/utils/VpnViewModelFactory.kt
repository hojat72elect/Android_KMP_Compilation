

package com.duckduckgo.common.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duckduckgo.common.utils.plugins.view_model.VpnViewModelFactoryPluginPoint
import com.duckduckgo.di.scopes.VpnScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(VpnScope::class)
@ContributesBinding(VpnScope::class)
class VpnViewModelFactory @Inject constructor(
    private val viewModelFactoryPluginPoint: VpnViewModelFactoryPluginPoint,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactoryPluginPoint.getPlugins().mapNotNull { it.create(modelClass) }
            .first()
    }
}
