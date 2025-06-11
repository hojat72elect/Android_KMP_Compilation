

package com.duckduckgo.subscriptions.impl.feedback

import android.os.Bundle
import android.view.View
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.subscriptions.impl.R
import com.duckduckgo.subscriptions.impl.databinding.ContentFeedbackGeneralBinding

@InjectWith(FragmentScope::class)
class SubscriptionFeedbackGeneralFragment : SubscriptionFeedbackFragment(R.layout.content_feedback_general) {
    private val binding: ContentFeedbackGeneralBinding by viewBinding()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val listener = activity as Listener

        binding.browserFeedback.setOnClickListener {
            listener.onBrowserFeedbackClicked()
        }

        binding.pproFeedback.setOnClickListener {
            listener.onPproFeedbackClicked()
        }
    }

    interface Listener {
        fun onBrowserFeedbackClicked()
        fun onPproFeedbackClicked()
    }

    companion object {
        internal fun instance(): SubscriptionFeedbackGeneralFragment = SubscriptionFeedbackGeneralFragment()
    }
}
