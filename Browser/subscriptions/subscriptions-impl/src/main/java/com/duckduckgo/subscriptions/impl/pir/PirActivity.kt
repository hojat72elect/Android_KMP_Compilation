

package com.duckduckgo.subscriptions.impl.pir

import android.os.Bundle
import com.duckduckgo.anvil.annotations.ContributeToActivityStarter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.store.AppTheme
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.macos.api.MacOsScreenWithEmptyParams
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.subscriptions.impl.R
import com.duckduckgo.subscriptions.impl.databinding.ActivityPirBinding
import com.duckduckgo.subscriptions.impl.pir.PirActivity.Companion.PirScreenWithEmptyParams
import com.duckduckgo.windows.api.ui.WindowsScreenWithEmptyParams
import javax.inject.Inject

@InjectWith(ActivityScope::class)
@ContributeToActivityStarter(PirScreenWithEmptyParams::class)
class PirActivity : DuckDuckGoActivity() {

    @Inject
    lateinit var globalActivityStarter: GlobalActivityStarter

    @Inject
    lateinit var appTheme: AppTheme

    private val binding: ActivityPirBinding by viewBinding()

    private val toolbar
        get() = binding.includeToolbar.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupInternalToolbar()
        setupListeners()
        if (appTheme.isLightModeEnabled()) {
            binding.container.setBackgroundResource(R.drawable.gradient_light)
        } else {
            binding.container.setBackgroundResource(R.drawable.gradient_dark)
        }
    }

    private fun setupListeners() {
        binding.appleButton.setOnClickListener {
            globalActivityStarter.start(this, MacOsScreenWithEmptyParams)
        }

        binding.windowsButton.setOnClickListener {
            globalActivityStarter.start(this, WindowsScreenWithEmptyParams)
        }
    }

    private fun setupInternalToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setTitle(null)
        toolbar.setNavigationIcon(com.duckduckgo.mobile.android.R.drawable.ic_arrow_left_24)
    }

    companion object {
        data object PirScreenWithEmptyParams : GlobalActivityStarter.ActivityParams
    }
}
