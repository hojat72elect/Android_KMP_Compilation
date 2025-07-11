

package com.duckduckgo.subscriptions.impl.feedback

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duckduckgo.common.ui.DuckDuckGoFragment
import com.duckduckgo.common.utils.FragmentViewModelFactory
import javax.inject.Inject

abstract class SubscriptionFeedbackFragment(@LayoutRes contentLayoutId: Int = 0) : DuckDuckGoFragment(contentLayoutId) {

    @Inject
    lateinit var viewModelFactory: FragmentViewModelFactory

    protected inline fun <reified V : ViewModel> bindViewModel() = lazy { ViewModelProvider(this, viewModelFactory).get(V::class.java) }
}
