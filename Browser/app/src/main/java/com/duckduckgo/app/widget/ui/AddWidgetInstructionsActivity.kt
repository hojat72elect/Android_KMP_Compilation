

package com.duckduckgo.app.widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.databinding.ActivityAddWidgetInstructionsBinding
import com.duckduckgo.app.widget.ui.AddWidgetInstructionsViewModel.Command.Close
import com.duckduckgo.app.widget.ui.AddWidgetInstructionsViewModel.Command.ShowHome
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope

@InjectWith(ActivityScope::class)
class AddWidgetInstructionsActivity : DuckDuckGoActivity() {

    private val binding: ActivityAddWidgetInstructionsBinding by viewBinding()

    private val viewModel: AddWidgetInstructionsViewModel by bindViewModel()

    private val instructionsButtons
        get() = binding.includeAddWidgetInstructionButtons

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureListeners()
        configureCommandObserver()
    }

    private fun configureListeners() {
        instructionsButtons.homeButton.setOnClickListener {
            viewModel.onShowHomePressed()
        }
        instructionsButtons.closeButton.setOnClickListener {
            viewModel.onClosePressed()
        }
    }

    private fun configureCommandObserver() {
        viewModel.command.observe(this) {
            when (it) {
                ShowHome -> showHome()
                Close -> close()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.onClosePressed()
    }

    fun showHome() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    fun close() {
        finishAfterTransition()
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, AddWidgetInstructionsActivity::class.java)
        }
    }
}
