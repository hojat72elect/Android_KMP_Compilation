

package com.duckduckgo.common.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duckduckgo.common.utils.plugins.view_model.ViewViewModelFactoryPluginPoint
import com.duckduckgo.di.scopes.ViewScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(ViewScope::class)
@ContributesBinding(ViewScope::class)
class ViewViewModelFactory @Inject constructor(
    private val viewModelFactoryPluginPoint: ViewViewModelFactoryPluginPoint,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactoryPluginPoint.getPlugins().mapNotNull { it.create(modelClass) }
            .first()
    }
}
