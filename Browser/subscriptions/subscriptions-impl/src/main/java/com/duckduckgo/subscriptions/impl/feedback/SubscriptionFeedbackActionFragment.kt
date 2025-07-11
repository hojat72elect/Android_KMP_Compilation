

package com.duckduckgo.subscriptions.impl.feedback

import android.os.Bundle
import android.view.View
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.subscriptions.impl.R
import com.duckduckgo.subscriptions.impl.databinding.ContentFeedbackActionBinding
import com.duckduckgo.subscriptions.impl.feedback.SubscriptionFeedbackReportType.GENERAL_FEEDBACK
import com.duckduckgo.subscriptions.impl.feedback.SubscriptionFeedbackReportType.REPORT_PROBLEM
import com.duckduckgo.subscriptions.impl.feedback.SubscriptionFeedbackReportType.REQUEST_FEATURE

@InjectWith(FragmentScope::class)
class SubscriptionFeedbackActionFragment : SubscriptionFeedbackFragment(R.layout.content_feedback_action) {
    private val binding: ContentFeedbackActionBinding by viewBinding()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val listener = activity as Listener

        binding.generalFeedback.setOnClickListener {
            listener.onUserClickedReportType(GENERAL_FEEDBACK)
        }

        binding.reportProblem.setOnClickListener {
            listener.onUserClickedReportType(REPORT_PROBLEM)
        }

        binding.requestFeature.setOnClickListener {
            listener.onUserClickedReportType(REQUEST_FEATURE)
        }
    }

    interface Listener {
        fun onUserClickedReportType(reportType: SubscriptionFeedbackReportType)
    }

    companion object {
        internal fun instance(): SubscriptionFeedbackActionFragment = SubscriptionFeedbackActionFragment()
    }
}
