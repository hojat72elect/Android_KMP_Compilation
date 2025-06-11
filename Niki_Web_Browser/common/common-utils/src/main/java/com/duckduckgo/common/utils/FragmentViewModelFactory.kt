

package com.duckduckgo.common.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duckduckgo.common.utils.plugins.view_model.FragmentViewModelFactoryPluginPoint
import com.duckduckgo.di.scopes.FragmentScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(FragmentScope::class)
@ContributesBinding(FragmentScope::class)
class FragmentViewModelFactory @Inject constructor(
    private val viewModelFactoryPluginPoint: FragmentViewModelFactoryPluginPoint,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactoryPluginPoint.getPlugins().mapNotNull { it.create(modelClass) }
            .first()
    }
}
