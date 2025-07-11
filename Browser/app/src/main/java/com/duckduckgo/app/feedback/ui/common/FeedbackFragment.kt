

package com.duckduckgo.app.feedback.ui.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duckduckgo.common.ui.DuckDuckGoFragment
import com.duckduckgo.common.utils.FragmentViewModelFactory
import javax.inject.Inject

abstract class FeedbackFragment(@LayoutRes contentLayoutId: Int = 0) : DuckDuckGoFragment(contentLayoutId) {

    @Inject
    lateinit var viewModelFactory: FragmentViewModelFactory

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureListeners()
        configureViewModelObservers()
    }

    open fun configureViewModelObservers() {}
    open fun configureListeners() {}

    protected inline fun <reified V : ViewModel> bindViewModel() = lazy { ViewModelProvider(this, viewModelFactory).get(V::class.java) }
}
