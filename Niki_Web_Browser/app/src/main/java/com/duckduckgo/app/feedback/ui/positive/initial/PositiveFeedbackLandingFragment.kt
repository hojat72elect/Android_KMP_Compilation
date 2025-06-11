

package com.duckduckgo.app.feedback.ui.positive.initial

import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.databinding.ContentFeedbackPositiveLandingBinding
import com.duckduckgo.app.feedback.ui.common.FeedbackFragment
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.playstore.PlayStoreUtils
import com.duckduckgo.di.scopes.FragmentScope
import javax.inject.Inject

@InjectWith(FragmentScope::class)
class PositiveFeedbackLandingFragment : FeedbackFragment(R.layout.content_feedback_positive_landing) {

    interface PositiveFeedbackLandingListener {
        fun userSelectedToRateApp()
        fun userSelectedToGiveFeedback()
        fun userGavePositiveFeedbackNoDetails()
    }

    private val binding: ContentFeedbackPositiveLandingBinding by viewBinding()

    private val viewModel by bindViewModel<PositiveFeedbackLandingViewModel>()

    private val listener: PositiveFeedbackLandingListener?
        get() = activity as PositiveFeedbackLandingListener

    @Inject
    lateinit var playStoreUtils: PlayStoreUtils

    override fun configureViewModelObservers() {
        viewModel.command.observe(this) { command ->
            when (command) {
                Command.LaunchPlayStore -> {
                    launchPlayStore()
                    listener?.userSelectedToRateApp()
                }
                Command.Exit -> {
                    listener?.userGavePositiveFeedbackNoDetails()
                }
                Command.LaunchShareFeedbackPage -> {
                    listener?.userSelectedToGiveFeedback()
                }
            }
        }
    }

    override fun configureListeners() {
        binding.rateAppButton.setOnClickListener { viewModel.userSelectedToRateApp() }
        binding.shareFeedbackButton.setOnClickListener { viewModel.userSelectedToProvideFeedbackDetails() }
        binding.cancelButton.setOnClickListener { viewModel.userFinishedGivingPositiveFeedback() }
    }

    private fun launchPlayStore() {
        playStoreUtils.launchPlayStore()
    }

    companion object {
        fun instance(): PositiveFeedbackLandingFragment {
            return PositiveFeedbackLandingFragment()
        }
    }
}
