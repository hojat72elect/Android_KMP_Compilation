

package com.duckduckgo.app.dev.settings.tabs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.databinding.ActivityDevTabsBinding
import com.duckduckgo.app.dev.settings.tabs.DevTabsViewModel.ViewState
import com.duckduckgo.app.notification.NotificationFactory
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InjectWith(ActivityScope::class)
class DevTabsActivity : DuckDuckGoActivity() {

    @Inject
    lateinit var viewModel: DevTabsViewModel

    @Inject
    lateinit var factory: NotificationFactory

    private val binding: ActivityDevTabsBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(binding.includeToolbar.toolbar)

        binding.addTabsButton.setOnClickListener {
            viewModel.addTabs(binding.tabCount.text.toString().toInt())
        }

        binding.clearTabsButton.setOnClickListener {
            viewModel.clearTabs()
        }

        observeViewState()
    }

    private fun observeViewState() {
        viewModel.viewState.flowWithLifecycle(lifecycle, STARTED).onEach { render(it) }
            .launchIn(lifecycleScope)
    }

    private fun render(viewState: ViewState) {
        binding.tabCountHeader.text = getString(R.string.devSettingsTabsScreenHeader, viewState.tabCount)
    }

    companion object {

        fun intent(context: Context): Intent {
            return Intent(context, DevTabsActivity::class.java)
        }
    }
}
