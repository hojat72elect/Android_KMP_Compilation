

package com.duckduckgo.sync.impl.ui.setup

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.DuckDuckGoFragment
import com.duckduckgo.common.ui.view.hide
import com.duckduckgo.common.ui.view.show
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.FragmentViewModelFactory
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.sync.impl.R
import com.duckduckgo.sync.impl.databinding.FragmentIntroSyncBinding
import com.duckduckgo.sync.impl.ui.setup.SetupAccountActivity.Companion.Screen
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.Command
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.Command.AbortFlow
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.Command.RecoverDataFlow
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.Command.StartSetupFlow
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.ViewMode.CreateAccountIntro
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.ViewMode.RecoverAccountIntro
import com.duckduckgo.sync.impl.ui.setup.SyncSetupIntroViewModel.ViewState
import javax.inject.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@InjectWith(FragmentScope::class)
class SyncSetupIntroFragment : DuckDuckGoFragment(R.layout.fragment_intro_sync) {
    @Inject
    lateinit var viewModelFactory: FragmentViewModelFactory

    private val binding: FragmentIntroSyncBinding by viewBinding()

    private val viewModel: SyncSetupIntroViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SyncSetupIntroViewModel::class.java]
    }

    private val listener: SetupFlowListener?
        get() = activity as? SetupFlowListener

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        configureListeners()
        observeUiEvents()
    }

    private fun configureListeners() {
        binding.closeIcon.setOnClickListener {
            viewModel.onAbortClicked()
        }
    }

    private fun observeUiEvents() {
        viewModel
            .viewState(getScreen())
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { viewState -> renderViewState(viewState) }
            .launchIn(lifecycleScope)

        viewModel
            .commands()
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { processCommand(it) }
            .launchIn(lifecycleScope)
    }

    private fun renderViewState(viewState: ViewState) {
        when (viewState.viewMode) {
            is CreateAccountIntro -> {
                binding.contentTitle.text = getString(R.string.sync_intro_enable_title)
                binding.contentBody.text = getString(R.string.sync_intro_enable_content)
                binding.contentIllustration.setImageResource(R.drawable.ic_sync_server_128)
                binding.syncIntroCta.text = getString(R.string.sync_intro_enable_cta)
                binding.syncIntroFooter.text = getString(R.string.sync_intro_enable_footer)
                binding.syncIntroFooter.show()
                binding.syncIntroCta.setOnClickListener {
                    viewModel.onTurnSyncOnClicked()
                }
            }

            is RecoverAccountIntro -> {
                binding.contentTitle.text = getString(R.string.sync_intro_recover_title)
                binding.contentBody.text = getString(R.string.sync_intro_recover_content)
                binding.contentIllustration.setImageResource(R.drawable.ic_sync_recover_128)
                binding.syncIntroFooter.hide()
                binding.syncIntroCta.text = getString(R.string.sync_intro_recover_cta)
                binding.syncIntroCta.setOnClickListener {
                    viewModel.onStartRecoverDataClicked()
                }
            }
        }
    }

    private fun processCommand(it: Command) {
        when (it) {
            AbortFlow -> {
                requireActivity().setResult(Activity.RESULT_CANCELED)
                requireActivity().finish()
            }
            StartSetupFlow -> listener?.launchCreateAccountScreen()
            RecoverDataFlow -> listener?.launchRecoverAccountScreen()
        }
    }

    private fun getScreen(): Screen {
        return requireArguments().getSerializable(KEY_CREATE_ACCOUNT_INTRO) as Screen
    }

    companion object {

        const val KEY_CREATE_ACCOUNT_INTRO = "KEY_CREATE_ACCOUNT_INTRO"

        fun instance(screen: Screen): SyncSetupIntroFragment {
            Timber.d("Sync-Setup: screen $screen")
            val fragment = SyncSetupIntroFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CREATE_ACCOUNT_INTRO, screen)
            fragment.arguments = bundle
            return fragment
        }
    }
}
