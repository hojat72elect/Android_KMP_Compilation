

package com.duckduckgo.app.generalsettings.showonapplaunch

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duckduckgo.anvil.annotations.ContributeToActivityStarter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.databinding.ActivityShowOnAppLaunchSettingBinding
import com.duckduckgo.app.generalsettings.showonapplaunch.model.ShowOnAppLaunchOption.LastOpenedTab
import com.duckduckgo.app.generalsettings.showonapplaunch.model.ShowOnAppLaunchOption.NewTabPage
import com.duckduckgo.app.generalsettings.showonapplaunch.model.ShowOnAppLaunchOption.SpecificPage
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InjectWith(ActivityScope::class)
@ContributeToActivityStarter(ShowOnAppLaunchScreenNoParams::class)
class ShowOnAppLaunchActivity : DuckDuckGoActivity() {

    private val viewModel: ShowOnAppLaunchViewModel by bindViewModel()
    private val binding: ActivityShowOnAppLaunchSettingBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupToolbar(binding.includeToolbar.toolbar)

        binding.specificPageUrlInput.setSelectAllOnFocus(true)

        configureUiEventHandlers()
        observeViewModel()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setSpecificPageUrl(binding.specificPageUrlInput.text)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureUiEventHandlers() {
        binding.lastOpenedTabCheckListItem.setClickListener {
            viewModel.onShowOnAppLaunchOptionChanged(LastOpenedTab)
        }

        binding.newTabCheckListItem.setClickListener {
            viewModel.onShowOnAppLaunchOptionChanged(NewTabPage)
        }

        binding.specificPageCheckListItem.setClickListener {
            viewModel.onShowOnAppLaunchOptionChanged(SpecificPage(binding.specificPageUrlInput.text))
        }

        binding.specificPageUrlInput.addFocusChangedListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.onShowOnAppLaunchOptionChanged(
                    SpecificPage(binding.specificPageUrlInput.text),
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.viewState
            .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { viewState ->
                when (viewState.selectedOption) {
                    LastOpenedTab -> {
                        uncheckNewTabCheckListItem()
                        uncheckSpecificPageCheckListItem()
                        binding.lastOpenedTabCheckListItem.setChecked(true)
                    }
                    NewTabPage -> {
                        uncheckLastOpenedTabCheckListItem()
                        uncheckSpecificPageCheckListItem()
                        binding.newTabCheckListItem.setChecked(true)
                    }
                    is SpecificPage -> {
                        uncheckLastOpenedTabCheckListItem()
                        uncheckNewTabCheckListItem()
                        with(binding) {
                            specificPageCheckListItem.setChecked(true)
                            specificPageUrlInput.isEnabled = true
                        }
                    }
                }

                if (binding.specificPageUrlInput.text.isBlank()) {
                    binding.specificPageUrlInput.text = viewState.specificPageUrl
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun uncheckLastOpenedTabCheckListItem() {
        binding.lastOpenedTabCheckListItem.setChecked(false)
    }

    private fun uncheckNewTabCheckListItem() {
        binding.newTabCheckListItem.setChecked(false)
    }

    private fun uncheckSpecificPageCheckListItem() {
        binding.specificPageCheckListItem.setChecked(false)
        binding.specificPageUrlInput.isEnabled = false
    }
}
