

package com.duckduckgo.common.utils.plugins.view_model

import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.DaggerSet
import com.duckduckgo.di.scopes.FragmentScope
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(FragmentScope::class)
class FragmentViewModelFactoryPluginPoint @Inject constructor(
    private val injectorPlugins: DaggerSet<ViewModelFactoryPlugin>,
) : PluginPoint<ViewModelFactoryPlugin> {
    override fun getPlugins(): List<ViewModelFactoryPlugin> {
        return injectorPlugins.toList()
    }
}
