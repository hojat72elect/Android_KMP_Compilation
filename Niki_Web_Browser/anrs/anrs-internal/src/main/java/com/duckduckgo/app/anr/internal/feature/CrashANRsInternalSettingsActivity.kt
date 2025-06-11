

package com.duckduckgo.app.anr.internal.feature

import android.os.Bundle
import com.duckduckgo.anvil.annotations.ContributeToActivityStarter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.anr.internal.databinding.ActivityCrashAnrInternalSettingsBinding
import com.duckduckgo.app.anr.internal.feature.CrashANRsInternalScreens.InternalCrashSettings
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.navigation.api.GlobalActivityStarter
import javax.inject.Inject

@InjectWith(ActivityScope::class)
@ContributeToActivityStarter(InternalCrashSettings::class)
class CrashANRsInternalSettingsActivity : DuckDuckGoActivity() {

    @Inject
    lateinit var settings: PluginPoint<CrashANRsSettingPlugin>

    private val binding: ActivityCrashAnrInternalSettingsBinding by viewBinding()

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
                binding.settingsContent.addView(remoteViewPlugin)
            }
    }
}

sealed class CrashANRsInternalScreens : GlobalActivityStarter.ActivityParams {
    data object InternalCrashSettings : CrashANRsInternalScreens() {
        private fun readResolve(): Any = InternalCrashSettings
    }
}
