

package com.duckduckgo.subscriptions.internal

import android.os.Bundle
import com.duckduckgo.anvil.annotations.ContributeToActivityStarter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.subscriptions.internal.SubsInternalScreens.InternalSettings
import com.duckduckgo.subscriptions.internal.databinding.ActivitySubsInternalSettingsBinding
import javax.inject.Inject

@InjectWith(ActivityScope::class)
@ContributeToActivityStarter(InternalSettings::class)
class SubscriptionsInternalSettingsActivity : DuckDuckGoActivity() {

    @Inject
    lateinit var settings: PluginPoint<SubsSettingPlugin>

    @Inject lateinit var globalActivityStarter: GlobalActivityStarter

    private val binding: ActivitySubsInternalSettingsBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(binding.toolbar)

        setupUiElementState()
    }

    private fun setupUiElementState() {
        settings.getPlugins()
            .mapNotNull { it.getView(this) }
            .forEach { remoteViewPlugin ->
                binding.subsSettingsContent.addView(remoteViewPlugin)
            }
    }
}

sealed class SubsInternalScreens : GlobalActivityStarter.ActivityParams {
    data object InternalSettings : SubsInternalScreens() {
        private fun readResolve(): Any = InternalSettings
    }
}
