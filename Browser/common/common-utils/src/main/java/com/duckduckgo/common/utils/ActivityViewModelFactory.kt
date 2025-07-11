

package com.duckduckgo.common.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duckduckgo.common.utils.plugins.view_model.ActivityViewModelFactoryPluginPoint
import com.duckduckgo.di.scopes.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(ActivityScope::class)
@ContributesBinding(ActivityScope::class)
class ActivityViewModelFactory @Inject constructor(
    private val viewModelFactoryPluginPoint: ActivityViewModelFactoryPluginPoint,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactoryPluginPoint.getPlugins().mapNotNull { it.create(modelClass) }
            .first()
    }
}
