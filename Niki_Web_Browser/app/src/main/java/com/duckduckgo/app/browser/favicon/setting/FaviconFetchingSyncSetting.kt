

package com.duckduckgo.app.browser.favicon.setting

import android.content.*
import android.util.*
import android.widget.*
import androidx.lifecycle.*
import com.duckduckgo.anvil.annotations.*
import com.duckduckgo.app.browser.databinding.ViewSyncFaviconsFetchingBinding
import com.duckduckgo.app.browser.favicon.setting.FaviconFetchingViewModel.ViewState
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.*
import com.duckduckgo.saved.sites.impl.databinding.*
import dagger.android.support.*
import javax.inject.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InjectWith(ViewScope::class)
class FaviconFetchingSyncSetting @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    @Inject
    lateinit var viewModelFactory: FaviconFetchingViewModel.Factory

    @Inject
    lateinit var dispatchers: DispatcherProvider

    private var job: ConflatedJob = ConflatedJob()

    private val binding: ViewSyncFaviconsFetchingBinding by viewBinding()

    private val viewModel: FaviconFetchingViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!, viewModelFactory)[FaviconFetchingViewModel::class.java]
    }

    override fun onAttachedToWindow() {
        AndroidSupportInjection.inject(this)
        super.onAttachedToWindow()

        binding.syncFaviconsFetching.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.onFaviconFetchingSettingChanged(isChecked)
        }

        job += viewModel.viewState()
            .onEach { render(it) }
            .launchIn(findViewTreeLifecycleOwner()?.lifecycleScope!!)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

    private fun render(it: ViewState) {
        binding.syncFaviconsFetching.setIsChecked(it.faviconsFetchingEnabled)
    }
}
