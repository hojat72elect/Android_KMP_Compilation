

package com.duckduckgo.common.ui.internal.experiments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.internal.databinding.ActivityExperimentalUiSettingsBinding
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject

@InjectWith(ActivityScope::class)
class UIExperimentsActivity : DuckDuckGoActivity() {

    private val binding: ActivityExperimentalUiSettingsBinding by viewBinding()

    @Inject
    lateinit var experimentalUIPlugins: PluginPoint<ExperimentalUIPlugin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupToolbar(binding.includeToolbar.toolbar)

        experimentalUIPlugins.getPlugins().forEach { plugin ->
            binding.experimentalUILayout.addView(
                plugin.getView(this),
                android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                ),
            )
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, UIExperimentsActivity::class.java)
        }
    }
}
