

package com.duckduckgo.common.ui

import android.content.Context
import androidx.annotation.LayoutRes
import dagger.android.DaggerFragment
import dagger.android.support.AndroidSupportInjection

abstract class DuckDuckGoFragment(@LayoutRes contentLayoutId: Int = 0) : DaggerFragment(contentLayoutId) {

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}
