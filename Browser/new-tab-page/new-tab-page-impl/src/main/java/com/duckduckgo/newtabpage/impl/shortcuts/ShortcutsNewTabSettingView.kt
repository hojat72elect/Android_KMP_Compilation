

package com.duckduckgo.newtabpage.impl.shortcuts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.ViewViewModelFactory
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.di.scopes.ViewScope
import com.duckduckgo.newtabpage.api.NewTabPageSection
import com.duckduckgo.newtabpage.api.NewTabPageSectionSettingsPlugin
import com.duckduckgo.newtabpage.impl.databinding.ViewNewTabShortcutsSettingItemBinding
import com.duckduckgo.newtabpage.impl.shortcuts.ShortcutsNewTabSettingsViewModel.ViewState
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InjectWith(ViewScope::class)
class ShortcutsNewTabSettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    @Inject
    lateinit var viewModelFactory: ViewViewModelFactory

    @Inject
    lateinit var dispatchers: DispatcherProvider

    private val binding: ViewNewTabShortcutsSettingItemBinding by viewBinding()

    private val viewModel: ShortcutsNewTabSettingsViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!, viewModelFactory)[ShortcutsNewTabSettingsViewModel::class.java]
    }

    private val conflatedJob = ConflatedJob()

    override fun onAttachedToWindow() {
        AndroidSupportInjection.inject(this)
        super.onAttachedToWindow()

        findViewTreeLifecycleOwner()?.lifecycle?.addObserver(viewModel)

        conflatedJob += viewModel.viewState
            .onEach { render(it) }
            .launchIn(findViewTreeLifecycleOwner()?.lifecycleScope!!)
    }

    override fun onDetachedFromWindow() {
        conflatedJob.cancel()
        super.onDetachedFromWindow()
    }

    private fun render(viewState: ViewState) {
        binding.root.quietlySetIsChecked(viewState.enabled) { _, enabled ->
            viewModel.onSettingEnabled(enabled)
        }
    }
}

@ContributesMultibinding(scope = ActivityScope::class)
@PriorityKey(NewTabPageSectionSettingsPlugin.SHORTCUTS)
class ShortcutsNewTabSettingViewPlugin @Inject constructor() : NewTabPageSectionSettingsPlugin {
    override val name = NewTabPageSection.SHORTCUTS.name

    override fun getView(context: Context): View {
        return ShortcutsNewTabSettingView(context)
    }

    override suspend fun isActive(): Boolean {
        return true
    }
}
