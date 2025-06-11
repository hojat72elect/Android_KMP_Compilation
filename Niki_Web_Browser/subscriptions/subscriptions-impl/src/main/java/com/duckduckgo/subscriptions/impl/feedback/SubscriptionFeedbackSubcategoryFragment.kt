

package com.duckduckgo.subscriptions.impl.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.mobile.android.databinding.RowOneLineListItemBinding
import com.duckduckgo.subscriptions.impl.R
import com.duckduckgo.subscriptions.impl.databinding.ContentFeedbackSubcategoryBinding
import javax.inject.Inject

@InjectWith(FragmentScope::class)
class SubscriptionFeedbackSubcategoryFragment : SubscriptionFeedbackFragment(R.layout.content_feedback_subcategory) {

    @Inject
    lateinit var feedbackSubCategoryProvider: FeedbackSubCategoryProvider

    private val binding: ContentFeedbackSubcategoryBinding by viewBinding()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val listener = activity as Listener
        val category = requireArguments().getSerializable(EXTRA_CATEGORY) as SubscriptionFeedbackCategory
        val layoutInflater = LayoutInflater.from(context)

        feedbackSubCategoryProvider.getSubCategories(category).forEach {
            val itemBinding = RowOneLineListItemBinding.inflate(layoutInflater, binding.subcategoryContainer, false)
            binding.subcategoryContainer.addView(itemBinding.root)
            itemBinding.root.setPrimaryText(getString(it.key))
            itemBinding.root.setClickListener {
                listener.onUserClickedSubCategory(it.value)
            }
        }
    }

    interface Listener {
        fun onUserClickedSubCategory(subCategory: SubscriptionFeedbackSubCategory)
    }

    companion object {

        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        internal fun instance(
            category: SubscriptionFeedbackCategory,
        ): SubscriptionFeedbackSubcategoryFragment = SubscriptionFeedbackSubcategoryFragment().apply {
            val args = Bundle()
            args.putSerializable(EXTRA_CATEGORY, category)
            arguments = args
        }
    }
}
