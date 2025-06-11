

package com.duckduckgo.app.feedback.ui.negative.mainreason

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.databinding.ContentFeedbackNegativeDisambiguationMainReasonBinding
import com.duckduckgo.app.feedback.ui.common.FeedbackFragment
import com.duckduckgo.app.feedback.ui.negative.FeedbackType.MainReason
import com.duckduckgo.app.feedback.ui.negative.FeedbackTypeDisplay
import com.duckduckgo.app.feedback.ui.negative.FeedbackTypeDisplay.FeedbackTypeMainReasonDisplay
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.FragmentScope

@InjectWith(FragmentScope::class)
class MainReasonNegativeFeedbackFragment : FeedbackFragment(R.layout.content_feedback_negative_disambiguation_main_reason) {
    private lateinit var recyclerAdapter: MainReasonAdapter

    interface MainReasonNegativeFeedbackListener {

        fun userSelectedNegativeFeedbackMainReason(type: MainReason)
    }

    private val binding: ContentFeedbackNegativeDisambiguationMainReasonBinding by viewBinding()

    private val listener: MainReasonNegativeFeedbackListener?
        get() = activity as MainReasonNegativeFeedbackListener

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerAdapter = MainReasonAdapter(
            object : (FeedbackTypeMainReasonDisplay) -> Unit {
                override fun invoke(reason: FeedbackTypeMainReasonDisplay) {
                    listener?.userSelectedNegativeFeedbackMainReason(reason.mainReason)
                }
            },
        )

        activity?.let {
            binding.recyclerView.layoutManager = LinearLayoutManager(it)
            binding.recyclerView.adapter = recyclerAdapter

            val listValues = getMainReasonsDisplayText()
            recyclerAdapter.submitList(listValues)
        }
    }

    private fun getMainReasonsDisplayText(): List<FeedbackTypeMainReasonDisplay> {
        return MainReason.values().mapNotNull {
            FeedbackTypeDisplay.mainReasons[it]
        }
    }

    companion object {

        fun instance(): MainReasonNegativeFeedbackFragment {
            return MainReasonNegativeFeedbackFragment()
        }
    }
}
