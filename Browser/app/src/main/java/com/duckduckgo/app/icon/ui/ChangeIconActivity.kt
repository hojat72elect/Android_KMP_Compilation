

package com.duckduckgo.app.icon.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.databinding.ActivityAppIconsBinding
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.view.dialog.TextAlertDialogBuilder
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.R as CommonR

@InjectWith(ActivityScope::class)
class ChangeIconActivity : DuckDuckGoActivity() {

    private val binding: ActivityAppIconsBinding by viewBinding()
    private val viewModel: ChangeIconViewModel by bindViewModel()
    private val iconsAdapter: AppIconsAdapter = AppIconsAdapter { icon ->
        viewModel.onIconSelected(icon)
    }

    private val toolbar
        get() = binding.includeToolbar.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(toolbar)
        configureRecycler()

        observeViewModel()
    }

    private fun configureRecycler() {
        binding.appIconsList.layoutManager = GridLayoutManager(this, 4)
        binding.appIconsList.addItemDecoration(ItemOffsetDecoration(this, CommonR.dimen.keyline_1))
        binding.appIconsList.adapter = iconsAdapter
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this) { viewState ->
            viewState?.let {
                render(it)
            }
        }

        viewModel.command.observe(this) {
            processCommand(it)
        }

        viewModel.start()
    }

    private fun render(viewState: ChangeIconViewModel.ViewState) {
        iconsAdapter.notifyChanges(viewState.appIcons)
    }

    private fun processCommand(it: ChangeIconViewModel.Command) {
        when (it) {
            is ChangeIconViewModel.Command.IconChanged -> {
                finish()
            }

            is ChangeIconViewModel.Command.ShowConfirmationDialog -> {
                TextAlertDialogBuilder(this)
                    .setTitle(R.string.changeIconDialogTitle)
                    .setMessage(getString(R.string.changeIconDialogMessage))
                    .setPositiveButton(R.string.changeIconCtaAccept)
                    .setNegativeButton(R.string.changeIconCtaCancel)
                    .addEventListener(
                        object : TextAlertDialogBuilder.EventListener() {
                            override fun onPositiveButtonClicked() {
                                viewModel.onIconConfirmed(it.viewData)
                            }
                        },
                    )
                    .show()
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, ChangeIconActivity::class.java)
        }
    }
}
