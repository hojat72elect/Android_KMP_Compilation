

package com.duckduckgo.app.feedback.ui.negative.brokensite

import androidx.core.view.doOnNextLayout
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.databinding.ContentFeedbackNegativeBrokenSiteFeedbackBinding
import com.duckduckgo.app.feedback.ui.common.FeedbackFragment
import com.duckduckgo.app.feedback.ui.common.LayoutScrollingTouchListener
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.FragmentScope

@InjectWith(FragmentScope::class)
class BrokenSiteNegativeFeedbackFragment : FeedbackFragment(R.layout.content_feedback_negative_broken_site_feedback) {

    interface BrokenSiteFeedbackListener {
        fun onProvidedBrokenSiteFeedback(
            feedback: String,
            url: String?,
        )

        fun userCancelled()
    }

    private val binding: ContentFeedbackNegativeBrokenSiteFeedbackBinding by viewBinding()

    private val viewModel by bindViewModel<BrokenSiteNegativeFeedbackViewModel>()

    private val listener: BrokenSiteFeedbackListener?
        get() = activity as BrokenSiteFeedbackListener

    override fun configureViewModelObservers() {
        viewModel.command.observe(this) { command ->
            when (command) {
                is BrokenSiteNegativeFeedbackViewModel.Command.Exit -> {
                    listener?.userCancelled()
                }
                is BrokenSiteNegativeFeedbackViewModel.Command.ExitAndSubmitFeedback -> {
                    listener?.onProvidedBrokenSiteFeedback(command.feedback, command.brokenSite)
                }
            }
        }
    }

    override fun configureListeners() {
        with(binding) {
            submitFeedbackButton.doOnNextLayout {
                brokenSiteInput.setOnTouchListener(LayoutScrollingTouchListener(rootScrollView, brokenSiteInput.y.toInt()))
                openEndedFeedback.setOnTouchListener(LayoutScrollingTouchListener(rootScrollView, openEndedFeedback.y.toInt()))
            }

            submitFeedbackButton.setOnClickListener {
                val feedback = openEndedFeedback.text
                val brokenSite = brokenSiteInput.text

                viewModel.userSubmittingFeedback(feedback, brokenSite)
            }
        }
    }

    companion object {
        fun instance(): BrokenSiteNegativeFeedbackFragment {
            return BrokenSiteNegativeFeedbackFragment()
        }
    }
}
